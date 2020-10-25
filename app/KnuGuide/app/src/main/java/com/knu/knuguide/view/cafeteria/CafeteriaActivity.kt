package com.knu.knuguide.view.cafeteria

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayout
import com.knu.knuguide.R
import com.knu.knuguide.support.KNUAdapterListener
import com.knu.knuguide.view.KNUActivity
import com.knu.knuguide.view.adapter.CafeteriaFragmentAdapter
import kotlinx.android.synthetic.main.activity_cafeteria.*

class CafeteriaActivity : KNUActivity(), KNUAdapterListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafeteria)

        setUpFragment()
    }

    private fun setUpFragment() {
        val cFragmentAdapter = CafeteriaFragmentAdapter(this@CafeteriaActivity)
        cafeteriaList.adapter = cFragmentAdapter

        TabLayoutMediator(tablayout, cafeteriaList) { tab, position ->
            tab.text = cFragmentAdapter.getTitle(position)
        }.attach()
    }

    override fun getKNUID(): String = KNU_ID

    companion object {
        const val KNU_ID = "CafeteriaActivity"
    }
}