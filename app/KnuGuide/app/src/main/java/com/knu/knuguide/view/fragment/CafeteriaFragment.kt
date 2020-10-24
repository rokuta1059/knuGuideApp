package com.knu.knuguide.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.knu.knuguide.R
import com.knu.knuguide.core.KNUService
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.cafeteria.Cafeteria
import com.knu.knuguide.view.adapter.RecyclerViewAdapter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.fragment_cafeteria.*

class CafeteriaFragment(var position: Int): Fragment() {

    private val compositeDisposable = CompositeDisposable()
    private var items = ArrayList<KNUData>()
    private lateinit var mAdapter: RecyclerViewAdapter
    private val layoutManager by lazy { LinearLayoutManager(context)}
    private val tabTextList = arrayListOf("재정", "새롬관", "이룸관")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cafeteria, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCafeteriaList(tabTextList[position])
        mAdapter = RecyclerViewAdapter(view.context, items)
        recyclerView_cafeteria.layoutManager = layoutManager
        recyclerView_cafeteria.adapter = mAdapter
    }

    private fun getCafeteriaList(id: String){
        compositeDisposable.add(KNUService.instance()!!.getCafeteriaById(id).subscribeWith(object : DisposableSingleObserver<List<Cafeteria>>() {
            override fun onSuccess(list: List<Cafeteria>) {
                items.clear()
                for(item in list){
                    when(getDomitory(id)){
                        0 -> item.type = Cafeteria.Type.JAEJEONG
                        1 -> item.type = Cafeteria.Type.SAEROM
                        2 -> item.type = Cafeteria.Type.IRUM
                    }
                    items.add(item)
                }
            }

            override fun onError(e: Throwable) {
                Snackbar.make(rootLayout, "네트워크 에러", Snackbar.LENGTH_SHORT).show()
                e.printStackTrace()
                Log.d("Error", e.message!!)
            }

            private fun getDomitory(id: String): Int{
                for(item in tabTextList)
                    if(item == id)
                        return tabTextList.indexOf(item)
                return 0
            }
        }))
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}