package com.knu.knuguide.view.main

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.knu.knuguide.R
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.announcement.Announcement
import com.knu.knuguide.support.FastClickPreventer
import com.knu.knuguide.view.KNUActivity
import com.knu.knuguide.view.adapter.PreviewAnnouncementAdapter
import com.knu.knuguide.view.adapter.decor.PreviewAnnouncementDecor
import com.knu.knuguide.view.announcement.AnnouncementActivity
import com.knu.knuguide.view.calendar.CalendarActivity
import com.knu.knuguide.view.speech.SpeechTestActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.knu_appbar.*
import kotlinx.android.synthetic.main.preview_announcement.*

class MainActivity : KNUActivity() {
    private val compositeDisposable = CompositeDisposable()
    private val fastClickPreventer = FastClickPreventer()
    private var items = ArrayList<KNUData>()

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
                R.id.speech -> {
                    navigateTo(SpeechTestActivity::class.java, null)
                }
            }
            true
        }

        more.setOnClickListener {
            if (fastClickPreventer.isClickable())
                navigateTo(AnnouncementActivity::class.java, null)
        }

        items.add(Announcement("SW중심사업단", "2020년 KNU SW중심대학 SW학습공동체 모집 안내"))
        items.add(Announcement("컴퓨터공학과", "2020학년도 제2차 교내 졸업자격인증 모의토익 시험일자 안내"))
        items.add(Announcement("컴퓨터공학과", "2020학년도 1학기 [멘토자격연수] 공고"))

        // 공지사항 아이템 추가
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PreviewAnnouncementAdapter(this, items)
        recyclerView.addItemDecoration(PreviewAnnouncementDecor(this, 1f))
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
