package com.knu.knuguide.view.announcement

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.knu.knuguide.R
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.announcement.Announcement
import com.knu.knuguide.support.FastClickPreventer
import com.knu.knuguide.view.KNUActivityCollapse
import com.knu.knuguide.view.adapter.AnnouncementAdapter
import com.knu.knuguide.view.adapter.decor.PreviewAnnouncementDecor
import kotlinx.android.synthetic.main.knu_appbar_collapse.*
import kotlinx.android.synthetic.main.preview_announcement.*
import java.util.*
import kotlin.collections.ArrayList

class AnnouncementActivity : KNUActivityCollapse() {
    private val fastClickPreventer = FastClickPreventer()
    private var items = ArrayList<KNUData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcement)

        appbar_back.setOnClickListener { if (fastClickPreventer.isClickable()) onBackPressed() }

        items.add(Announcement("SW중심사업단", "2020년 KNU SW중심대학 SW학습공동체 모집 안내"))
        items.add(Announcement("컴퓨터공학과", "2020학년도 제2차 교내 졸업자격인증 모의토익 시험일자 안내"))
        items.add(Announcement("컴퓨터공학과", "2020학년도 1학기 [멘토자격연수] 공고"))

        (items[0] as Announcement).date = Date()
        (items[1] as Announcement).date = Date()
        (items[2] as Announcement).date = Date()

        (items[0] as Announcement).type = Announcement.Type.GENERAL
        (items[1] as Announcement).type = Announcement.Type.GENERAL
        (items[2] as Announcement).type = Announcement.Type.GENERAL

        (items[0] as Announcement).isFavorite = true

        // 공지사항 아이템 추가
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AnnouncementAdapter(this, items)
        recyclerView.addItemDecoration(PreviewAnnouncementDecor(this, 6f))
    }

    override fun getKNUID(): String {
        return KNU_ID
    }

    companion object {
        const val KNU_ID = "AnnouncementActivity"
    }
}
