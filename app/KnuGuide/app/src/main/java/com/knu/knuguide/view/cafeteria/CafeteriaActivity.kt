package com.knu.knuguide.view.cafeteria

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.knu.knuguide.R
import com.knu.knuguide.data.cafeteria.Cafeteria
import com.knu.knuguide.support.KNUAdapterListener
import com.knu.knuguide.view.KNUActivity
import io.reactivex.disposables.CompositeDisposable

class CafeteriaActivity : KNUActivity(), KNUAdapterListener {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var tablayout: TabLayout
    private lateinit var irumList: List<Cafeteria>
    private lateinit var saeromList: List<Cafeteria>
    private lateinit var jaejungList: List<Cafeteria>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafeteria)

        val recycler: RecyclerView = findViewById(R.id.cafeteriaList)
        tablayout = findViewById(R.id.tablayout)
        tablayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {

            }
            override fun onTabUnselected(p0: TabLayout.Tab?) { }
        })
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