package com.knu.knuguide.view

import androidx.core.view.isVisible
import com.knu.knuguide.view.announcement.AnnouncementActivity
import com.knu.knuguide.view.calendar.CalendarActivity
import com.knu.knuguide.view.main.MainActivity
import kotlinx.android.synthetic.main.knu_appbar_collapse.*

abstract class KNUActivityCollapse : KNUActivity() {

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        initActionBar(getKNUID())
        updateActionBar(getKNUID())
    }

    private fun initActionBar(KNU_ID: String) {
        when (KNU_ID) {
            MainActivity.KNU_ID,
            AnnouncementActivity.KNU_ID,
            CalendarActivity.KNU_ID -> {
                setSupportActionBar(toolbar)
            }
        }
    }

    private fun updateActionBar(KNU_ID: String) {
        when (KNU_ID) {
            MainActivity.KNU_ID -> {
                setActionBarCustomView(KNU_ID)
                setActionBarTitle(false, "", "")
            }
            AnnouncementActivity.KNU_ID -> {
                setActionBarCustomView(KNU_ID)
                setActionBarTitle(true, "공지사항", "")
            }
            CalendarActivity.KNU_ID -> {
                setActionBarCustomView(KNU_ID)
                setActionBarTitle(true, "학사일정", "")
            }
        }
    }

    private fun setActionBarCustomView(KNU_ID: String) {
        when (KNU_ID) {
            MainActivity.KNU_ID -> {
                menu.isVisible = true
            }
            AnnouncementActivity.KNU_ID,
            CalendarActivity.KNU_ID -> {
                back.isVisible = true
            }
        }
    }

    private fun setActionBarTitle(visiblility: Boolean, title: String, subtitle: String) {
        toolbar_title.isVisible = visiblility
        toolbar_title.text = title
    }
}