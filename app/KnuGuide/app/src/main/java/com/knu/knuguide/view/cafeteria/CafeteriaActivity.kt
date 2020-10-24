package com.knu.knuguide.view.cafeteria

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayout
import com.knu.knuguide.R
import com.knu.knuguide.support.KNUAdapterListener
import com.knu.knuguide.view.KNUActivity
import com.knu.knuguide.view.adapter.CafeteriaAdapter
import kotlinx.android.synthetic.main.activity_cafeteria.*

class CafeteriaActivity : KNUActivity(), KNUAdapterListener {

    private lateinit var tablayout: TabLayout
    private val tabTextList = arrayListOf("재정", "새롬관", "이룸관")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafeteria)
        cafeteriaList.adapter = CafeteriaAdapter(this)
        tablayout = findViewById(R.id.tablayout)
        TabLayoutMediator(tablayout,cafeteriaList){
                tab,position -> tab.text = tabTextList[position]
        }.attach()
    }

    override fun getKNUID(): String = KNU_ID

    companion object {
        const val KNU_ID = "CafeteriaActivity"
    }
}