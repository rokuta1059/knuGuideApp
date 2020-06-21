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
import com.knu.knuguide.view.search.SearchActivity
import kotlinx.android.synthetic.main.knu_appbar_collapse.*
import kotlinx.android.synthetic.main.preview_announcement.*
import java.util.*
import kotlin.collections.ArrayList

class AnnouncementActivity : KNUActivityCollapse() {
    private val fastClickPreventer = FastClickPreventer()
    private var items = ArrayList<KNUData>()

    /**
     * todo: 1. 즐겨찾기 / 검색
     *       2. 사용자가 마지막으로 보려고 선택한 과가 Default
     *       3. 없다면, 즐겨찾기의 첫 번째 과로 Select
     *       4. 즐겨찾기도 없다면 검색창을 바로 Popup 또는 과를 선택해달라는 글을 띄움
     *
     * todo: 1. 다 그리지말고 개수 제한하여 Scroll 시 마다 그리는 법 알아보기
     *
     * todo: 1. 선택된 과 코드를 파라미터로 통신
     *       2. 결과를 객체화하여 Item 추가
     *       3. number : "공지"인 item이 맨 위로, 노란색 배경
     *       4. 나머지가 하늘색 배경
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcement)

        appbar_back.setOnClickListener { if (fastClickPreventer.isClickable()) onBackPressed() }
        appbar_search.setOnClickListener { if (fastClickPreventer.isClickable()) navigateTo(SearchActivity::class.java, null) }
        appbar_search_collapsed.setOnClickListener { if (fastClickPreventer.isClickable()) navigateTo(SearchActivity::class.java, null) }

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
