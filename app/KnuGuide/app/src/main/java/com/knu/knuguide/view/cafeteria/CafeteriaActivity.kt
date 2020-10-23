package com.knu.knuguide.view.cafeteria

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayout
import com.knu.knuguide.R
import com.knu.knuguide.data.cafeteria.Cafeteria
import com.knu.knuguide.support.KNUAdapterListener
import com.knu.knuguide.view.KNUActivity
import com.knu.knuguide.view.adapter.CafeteriaAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_cafeteria.*

class CafeteriaActivity : KNUActivity(), KNUAdapterListener {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var tablayout: TabLayout
    private lateinit var irumList: List<Cafeteria>
    private lateinit var saeromList: List<Cafeteria>
    private lateinit var jaejeongList: List<Cafeteria>
    private val tabTextList = arrayListOf("재정", "새롬관", "이룸관")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafeteria)

        cafeteriaList.adapter = CafeteriaAdapter(this)
        tablayout = findViewById(R.id.tablayout)
        TabLayoutMediator(tablayout,cafeteriaList){
                tab,position->
            tab.text = tabTextList[position]
        }.attach()
    }
    
    private fun getCafeteriaList(id: String){

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun getKNUID(): String = KNU_ID

    companion object {
        const val KNU_ID = "CafeteriaActivity"
    }
}