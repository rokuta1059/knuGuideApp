package com.knu.knuguide.view.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.knu.knuguide.R
import com.knu.knuguide.core.KNUService
import com.knu.knuguide.core.PrefService
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.announcement.Announcement
import com.knu.knuguide.support.FastClickPreventer
import com.knu.knuguide.support.KNUAdapterListener
import com.knu.knuguide.view.KNUActivity
import com.knu.knuguide.view.WebViewActivity
import com.knu.knuguide.view.adapter.AnnouncementAdapter
import com.knu.knuguide.view.adapter.decor.PreviewAnnouncementDecor
import com.knu.knuguide.view.announcement.AnnouncementActivity
import com.knu.knuguide.view.bus.BusActivity
import com.knu.knuguide.view.bus.BusInfoActivity
import com.knu.knuguide.view.cafeteria.CafeteriaActivity
import com.knu.knuguide.view.calendar.CalendarActivity
import com.knu.knuguide.view.department.DepartmentActivity
import com.knu.knuguide.view.search.SearchActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.knu_appbar.*
import kotlinx.android.synthetic.main.preview_announcement.*
import kotlinx.android.synthetic.main.preview_announcement.recyclerView

class MainActivity : KNUActivity(), KNUAdapterListener, PrefService.PrefChangeListener {
    private val compositeDisposable = CompositeDisposable()
    private val fastClickPreventer = FastClickPreventer()
    private var items = ArrayList<KNUData>()

    private lateinit var favoriteId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // menu 클릭 시 Navigation View 열기
        appbar_menu.setOnClickListener {
            if (fastClickPreventer.isClickable())
                drawerLayout.openDrawer(nav_view)
        }

        nav_view.setNavigationItemSelectedListener { m ->
            drawerLayout.closeDrawers()
            when (m.itemId) {
                R.id.announcement -> {
                    navigateTo(AnnouncementActivity::class.java, null)
                }
                R.id.calender -> {
                    navigateTo(CalendarActivity::class.java, null)
                }
                R.id.department -> {
                    val bundle = Bundle()
                    bundle.putBoolean(DepartmentActivity.KEY_DEPARTMENT_INFO, true)
                    navigateTo(SearchActivity::class.java, bundle)
                }
                R.id.bus -> {
                    navigateTo(BusActivity::class.java, null)
                }
//                R.id.speech -> {
//                    navigateTo(SpeechTestActivity::class.java, null)
//                }
                R.id.cafeteria -> {
                    navigateTo(CafeteriaActivity::class.java, null);
                }
            }
            false
        }

        more.setOnClickListener {
            if (fastClickPreventer.isClickable())
                navigateTo(AnnouncementActivity::class.java, null)
        }

        favoriteId = PrefService.instance()!!.getFavoriteId()

        // 공지사항 아이템 추가
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AnnouncementAdapter(this, items, this)
        recyclerView.addItemDecoration(PreviewAnnouncementDecor(this, 2f))

        PrefService.instance()!!.register(this.javaClass.simpleName, this)

        getNotice()
    }

    private fun getNotice() {
        if (favoriteId.isEmpty()) {
            empty.visibility = View.VISIBLE
            items.clear()

            recyclerView.adapter!!.notifyDataSetChanged()
            return
        }
        empty.visibility = View.GONE

        // 공지사항 데이터 불러오기
        compositeDisposable.add(KNUService.instance()!!.getNotice(favoriteId).subscribeWith(object : DisposableSingleObserver<List<Announcement>>() {
            override fun onSuccess(list: List<Announcement>) {
                var cnt = 0
                items.clear()
                for (item in list) {
                    item.type = Announcement.Type.PREVIEW // 미리 보기 형식 지정
                    items.add(item)

                    cnt++
                    if (cnt > 4)
                        break
                }
                recyclerView.adapter!!.notifyDataSetChanged()
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

    override fun onChangePref(key: String, value: Any) {
        if (key == PrefService.DEPARTMENT_FAVORITE_KEY) {
            favoriteId = value as String

            getNotice()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun getKNUID(): String {
        return KNU_ID
    }

    companion object {
        const val KNU_ID = "MainActivity"
    }
}
