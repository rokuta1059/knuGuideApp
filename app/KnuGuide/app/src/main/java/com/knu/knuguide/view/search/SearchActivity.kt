package com.knu.knuguide.view.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.knu.knuguide.R
import com.knu.knuguide.core.KNUService
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.search.Department
import com.knu.knuguide.support.FastClickPreventer
import com.knu.knuguide.support.KNUAdapterListener
import com.knu.knuguide.view.KNUActivity
import com.knu.knuguide.view.adapter.SearchAdapter
import com.knu.knuguide.view.adapter.decor.SearchAdapterDecor
import com.knu.knuguide.view.announcement.AnnouncementActivity.Companion.KEY_DEPARTMENT
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.knu_appbar.*

class SearchActivity : KNUActivity(), KNUAdapterListener {
    private val fastClickPreventer = FastClickPreventer()
    private val compositeDisposable = CompositeDisposable()

    // RecyclerView Adapter
    lateinit var mAdapter: SearchAdapter

    // search items
    private var items = ArrayList<KNUData>()

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
        compositeDisposable.add(KNUService.instance()!!.getDepartment().subscribeWith(object : DisposableSingleObserver<List<Department>>() {
            override fun onSuccess(list: List<Department>) {
                items.clear()
                items.addAll(list)

                mAdapter.notifyDataSetChanged()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Log.d("Error", e.message)
            }
        }))
    }

    // compositeDisposable 해제
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun getKNUID(): String {
        return KNU_ID
    }

    companion object {
        const val KNU_ID = "SearchActivity"
    }

    /*
        item을 이전 activity에 넘겨준다.
     */
    override fun onSearchItemClick(item: Department) {
        val intent = Intent()
        intent.putExtra(KEY_DEPARTMENT, item)
        setResult(RESULT_OK, intent)
        finish()
    }
}
