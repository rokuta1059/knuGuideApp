package com.knu.knuguide.view.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.knu.knuguide.R
import com.knu.knuguide.core.KNUService
import com.knu.knuguide.core.PrefService
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.search.Department
import com.knu.knuguide.support.FastClickPreventer
import com.knu.knuguide.support.KNUAdapterListener
import com.knu.knuguide.view.KNUActivity
import com.knu.knuguide.view.adapter.SearchAdapter
import com.knu.knuguide.view.adapter.decor.SearchAdapterDecor
import com.knu.knuguide.view.announcement.AnnouncementActivity.Companion.KEY_DEPARTMENT
import com.knu.knuguide.view.bus.BusInfoActivity
import com.knu.knuguide.view.department.DepartmentActivity
import com.knu.knuguide.view.department.DepartmentActivity.Companion.KEY_DEPARTMENT_INFO
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.knu_appbar.*
import java.lang.Exception

class SearchActivity : KNUActivity(), KNUAdapterListener {
    private val fastClickPreventer = FastClickPreventer()
    private val compositeDisposable = CompositeDisposable()

    // RecyclerView Adapter
    lateinit var mAdapter: SearchAdapter

    // search items
    private var items = ArrayList<KNUData>()
    private var favoriteIds = ArrayList<String>()

    private var isDepartmentInfo = false

    //
    private lateinit var favoriteId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Click Listener
        appbar_back.setOnClickListener { if (fastClickPreventer.isClickable()) onBackPressed() }

        // Init Adapter
        mAdapter = SearchAdapter(this, items, this)
        recycler_view_search.layoutManager = LinearLayoutManager(this)
        recycler_view_search.adapter = mAdapter
        recycler_view_search.addItemDecoration(SearchAdapterDecor(this, 1F, 16F))

        try {
            val bundle = intent.extras
            if (bundle != null) {
                if (bundle.getBoolean(KEY_DEPARTMENT_INFO)) {
                    mAdapter.releaseFavorite()
                    isDepartmentInfo = true
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        // getNoticeId
        favoriteId = PrefService.instance()!!.getFavoriteId()
        setFavoriteIds()

        // 과 목록 불러오기
        getDepartment()

        // SearchView Event Listener 등록
        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mAdapter.filter.filter(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty())
                    mAdapter.filter.filter("")

                return true
            }
        })
    }

    private fun getDepartment() {
        progress_bar.startProgress()

        compositeDisposable.add(KNUService.instance()!!.getDepartment().subscribeWith(object : DisposableSingleObserver<List<Department>>() {
            override fun onSuccess(list: List<Department>) {
                progress_bar.stopProgress()

                items.clear()
                for (item in list) {
                    if (favoriteIds.contains(item.id))
                        item.isFavorite = true

                    items.add(item)
                }
                items.sortWith(object : Comparator<KNUData> {
                    override fun compare(o1: KNUData?, o2: KNUData?): Int {
                        val d1 = o1 as Department
                        val d2 = o2 as Department

                        if (d1.isFavorite != d2.isFavorite) {
                            return if (d1.isFavorite) -1
                            else 1
                        }
                        return 0
                    }
                })

                mAdapter.notifyDataSetChanged()
            }

            override fun onError(e: Throwable) {
                progress_bar.stopProgress()

                e.printStackTrace()
                Log.d("Error", e.message)
            }
        }))
    }

    private fun setFavoriteIds() {
        try {
            for (i in favoriteId.indices step 3) {
                if (i+3 <= favoriteId.length) {
                    val id = favoriteId.substring(i, i+3)

                    favoriteIds.add(id)
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveFavorite() {
        val sb = StringBuilder()

        for (item in items) {
            val department = item as Department
            if (department.isFavorite)
                sb.append(department.id)
        }

        if (sb.toString().isEmpty())
            PrefService.instance()?.putFavoriteId("")
        else
            PrefService.instance()?.putFavoriteId(sb.toString())
    }

    // compositeDisposable 해제
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        saveFavorite()
    }

    override fun getKNUID(): String = KNU_ID

    companion object {
        const val KNU_ID = "SearchActivity"
    }

    /*
        item을 이전 activity에 넘겨준다.
     */
    override fun onSearchItemClick(item: Department) {
        if (isDepartmentInfo) {
            val bundle = Bundle().apply {
                putString(KEY_DEPARTMENT, item.id)
            }
            navigateTo(DepartmentActivity::class.java, bundle)
        }
        else {
            val intent = Intent()
            intent.putExtra(KEY_DEPARTMENT, item)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}
