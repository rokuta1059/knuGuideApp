package com.knu.knuguide.view.announcement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.knu.knuguide.R
import com.knu.knuguide.core.KNUService
import com.knu.knuguide.core.PrefService
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.announcement.Announcement
import com.knu.knuguide.data.search.Department
import com.knu.knuguide.support.FastClickPreventer
import com.knu.knuguide.support.KNUAdapterListener
import com.knu.knuguide.view.KNUActivityCollapse
import com.knu.knuguide.view.WebViewActivity
import com.knu.knuguide.view.adapter.AnnouncementAdapter
import com.knu.knuguide.view.adapter.decor.AnnouncementDecor
import com.knu.knuguide.view.search.SearchActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.activity_announcement.*
import kotlinx.android.synthetic.main.knu_appbar_collapse.*
import kotlinx.android.synthetic.main.preview_announcement.recyclerView
import java.io.Serializable

class AnnouncementActivity : KNUActivityCollapse(), KNUAdapterListener {
    private val fastClickPreventer = FastClickPreventer()
    private val compositeDisposable = CompositeDisposable()

    // RecyclerView Adapter
    lateinit var mAdapter: AnnouncementAdapter

    // RecyclerView Item
    private var items = ArrayList<KNUData>()

    //
    private lateinit var noticeId: String

    /**
     * todo: 1. 즐겨찾기 / 검색
     *       2. 사용자가 마지막으로 보려고 선택한 과가 Default
     *       3. 없다면, 즐겨찾기의 첫 번째 과로 Select
     *       4. 즐겨찾기도 없다면 검색창을 바로 Popup 또는 과를 선택해달라는 글을 띄움
     *
     * todo: 1. 선택된 과 코드를 파라미터로 통신
     *       2. 결과를 객체화하여 Item 추가
     *       3. number : "공지"인 item이 맨 위로, 노란색 배경
     *       4. 나머지가 하늘색 배경
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcement)

        // Click Listener
        appbar_back.setOnClickListener { if (fastClickPreventer.isClickable()) onBackPressed() }
        appbar_search.setOnClickListener { if (fastClickPreventer.isClickable()) navigateToForResult(SearchActivity::class.java, null, REQ_CODE_SEARCH_DEPARTMENT) }
        appbar_search_collapsed.setOnClickListener { if (fastClickPreventer.isClickable()) navigateToForResult(SearchActivity::class.java, null, REQ_CODE_SEARCH_DEPARTMENT) }

        // 공지사항 아이템 추가
        mAdapter = AnnouncementAdapter(this, items, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter
        recyclerView.addItemDecoration(AnnouncementDecor(this, 8f))

        // getNoticeId
        noticeId = PrefService.instance()!!.getNoticeId()

        if (!noticeId.isNullOrEmpty())
            setDepartmentById(noticeId)
    }

    private fun setDepartment(item: Department) {
        val id = item.id

        if (!id.isNullOrEmpty()) {
            noticeId = id // id 저장

            progress_bar.startProgress()
            // 공지사항 데이터 불러오기
            compositeDisposable.add(KNUService.instance()!!.getNotice(id).subscribeWith(object : DisposableSingleObserver<List<Announcement>>() {
                override fun onSuccess(list: List<Announcement>) {
                    progress_bar.stopProgress()
                    // set title text
                    department_collapsed.text = item.department
                    department_expanded.text = item.department

                    items.clear()
                    for (item in list) {
                        item.type = Announcement.Type.GENERAL // 일반 보기 형식 지정
                        items.add(item)
                    }
                    mAdapter.notifyDataSetChanged()
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    Log.d("Error", e.message)
                }
            }))
        }
    }

    private fun setDepartmentById(id: String) {
        progress_bar.startProgress()

        compositeDisposable.add(KNUService.instance()!!.getDepartmentById(id).subscribeWith(object : DisposableSingleObserver<List<Department>>() {
            override fun onSuccess(list: List<Department>) {
                if (list.isNotEmpty()) {
                    val item: Department = list[0]

                    department_collapsed.text = item.department
                    department_expanded.text = item.department

                    // 공지사항 데이터 불러오기
                    compositeDisposable.add(KNUService.instance()!!.getNotice(id).subscribeWith(object : DisposableSingleObserver<List<Announcement>>() {
                        override fun onSuccess(list: List<Announcement>) {
                            progress_bar.stopProgress()

                            items.clear()
                            for (item in list) {
                                item.type = Announcement.Type.GENERAL // 일반 보기 형식 지정
                                items.add(item)
                            }
                            mAdapter.notifyDataSetChanged()
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                            Log.d("Error", e.message)
                        }
                    }))
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Log.d("Error", e.message)
            }
        }))
    }

    override fun onAnnouncementClick(item: Announcement) {
        val args = Bundle()
        args.putSerializable(WebViewActivity.KEY_DATA, item.link)
        navigateTo(WebViewActivity::class.java, args)
    }

    // 자원 해제
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        PrefService.instance()?.putNoticeId(noticeId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_CODE_SEARCH_DEPARTMENT) {
                val item = data?.getSerializableExtra(KEY_DEPARTMENT) as Department
                if (item != null) {
                    setDepartment(item)
                    recyclerView.smoothScrollToPosition(0)
                }
            }
        }
    }

    override fun getKNUID(): String {
        return KNU_ID
    }

    companion object {
        const val KNU_ID = "AnnouncementActivity"

        // REQ_CODE
        const val REQ_CODE_SEARCH_DEPARTMENT = 200

        // KEYS
        const val KEY_DEPARTMENT = "department"
    }
}
