package com.knu.knuguide.view.main

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.knu.knuguide.R
import com.knu.knuguide.core.KNUService
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.announcement.Announcement
import com.knu.knuguide.support.FastClickPreventer
import com.knu.knuguide.support.KNUAdapterListener
import com.knu.knuguide.view.KNUActivity
import com.knu.knuguide.view.adapter.AnnouncementAdapter
import com.knu.knuguide.view.adapter.decor.PreviewAnnouncementDecor
import com.knu.knuguide.view.announcement.AnnouncementActivity
import com.knu.knuguide.view.calendar.CalendarActivity
import com.knu.knuguide.view.speech.SpeechTestActivity
import com.orhanobut.logger.Logger
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.knu_appbar.*
import kotlinx.android.synthetic.main.preview_announcement.*
import okhttp3.ResponseBody

class MainActivity : KNUActivity(), KNUAdapterListener {
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

        // 공지사항 아이템 추가
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AnnouncementAdapter(this, items, this)
        recyclerView.addItemDecoration(PreviewAnnouncementDecor(this, 2f))
    }

    override fun getKNUID(): String {
        return KNU_ID
    }

    companion object {
        const val KNU_ID = "MainActivity"
    }
}
