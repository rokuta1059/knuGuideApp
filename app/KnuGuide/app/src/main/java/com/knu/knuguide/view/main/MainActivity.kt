package com.knu.knuguide.view.main

import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import com.knu.knuguide.R
import com.knu.knuguide.view.KNUActivityCollapse
import com.knu.knuguide.view.announcement.AnnouncementActivity
import com.knu.knuguide.view.calendar.CalendarActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.knu_appbar_collapse.*

class MainActivity : KNUActivityCollapse() {
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // menu 클릭 시 Navigation View 열기
        menu.setOnClickListener { drawerLayout.openDrawer(nav_view) }
        nav_view.setNavigationItemSelectedListener { m ->
            drawerLayout.closeDrawers()
            when (m.itemId) {
                R.id.announcement -> {
                    navigateTo(AnnouncementActivity::class.java, null)
                }
                R.id.calender -> {
                    navigateTo(CalendarActivity::class.java, null)
                }
            }
            true
        }
    }

//    fun getStations(stationName: String) {
//        compositeDisposable.add(KNUService.instance()!!.postStation(stationName)
//            .subscribeWith(object : DisposableSingleObserver<ArrayList<String>>() {
//                override fun onSuccess(t: ArrayList<String>) {
//                    station.text = ""
//
//                    for (name in t) {
//                        station.append(name + "\n")
//                    }
//                }
//                override fun onError(e: Throwable) {
//                    Logger.d("onError ${e.message}")
//                }
//            }))
//    }

    override fun getKNUID(): String {
        return KNU_ID
    }

    companion object {
        const val KNU_ID = "MainActivity"
    }
}
