package com.knu.knuguide.view

import androidx.core.view.isVisible
import com.google.android.material.appbar.AppBarLayout
import com.knu.knuguide.view.bus.BusActivity
import com.knu.knuguide.view.bus.BusInfoActivity
import com.knu.knuguide.view.calendar.CalendarActivity
import com.knu.knuguide.view.department.DepartmentActivity
import com.knu.knuguide.view.main.MainActivity
import com.knu.knuguide.view.search.SearchActivity
import kotlinx.android.synthetic.main.knu_appbar.*

abstract class KNUActivity : KNUBlankActivity() {
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        initActionBar(getKNUID())
        updateActionBar(getKNUID())
    }

    private fun initActionBar(KNU_ID: String) {
        when (KNU_ID) {
            MainActivity.KNU_ID,
            CalendarActivity.KNU_ID,
            SearchActivity.KNU_ID,
            WebViewActivity.KNU_ID,
            DepartmentActivity.KNU_ID,
            BusActivity.KNU_ID,
            BusInfoActivity.KNU_ID -> {
                setSupportActionBar(appbar)
            }
        }
    }

    private fun updateActionBar(KNU_ID: String) {
        when (KNU_ID) {
            MainActivity.KNU_ID -> {
                setActionBarCustomView(KNU_ID)
                setActionBarTitle(true, "곰두리 가이드", "")
            }
            CalendarActivity.KNU_ID -> {
                setActionBarCustomView(KNU_ID)
                setActionBarTitle(true, "학사일정", "")
                setActionBarScrollFlag()
            }
            SearchActivity.KNU_ID -> {
                setActionBarCustomView(KNU_ID)
                setActionBarTitle(false, "", "")
            }
            WebViewActivity.KNU_ID -> {
                setActionBarCustomView(KNU_ID)
                setActionBarTitle(false, "", "")
                setActionBarScrollFlag()
            }
            DepartmentActivity.KNU_ID -> {
                setActionBarCustomView(KNU_ID)
                setActionBarTitle(true, "학과정보", "")
                setActionBarBack()
            }
            BusActivity.KNU_ID -> {
                setActionBarCustomView(KNU_ID)
                setActionBarTitle(true, "강원대 버스", "")
                setActionBarBack()
            }

            BusInfoActivity.KNU_ID -> {
                setActionBarCustomView(KNU_ID)
                setActionBarTitle(true, "", "BUS")
                setActionBarBack()
            }
        }
    }

    private fun setActionBarCustomView(KNU_ID: String) {
        when (KNU_ID) {
            MainActivity.KNU_ID -> {
                appbar_menu.isVisible = true
            }
            CalendarActivity.KNU_ID -> {
                appbar_back.isVisible = true
            }
            SearchActivity.KNU_ID -> {
                appbar_back.isVisible = true
                searchview.isVisible = true
            }
            WebViewActivity.KNU_ID -> {
                appbar_back.isVisible = true
            }
            DepartmentActivity.KNU_ID -> {
                appbar_back.isVisible = true
            }
            BusActivity.KNU_ID -> {
                appbar_back.isVisible = true
            }
            BusInfoActivity.KNU_ID -> {
                appbar_back.isVisible = true
            }
        }
    }

    private fun setActionBarTitle(visiblility: Boolean, title: String, subtitle: String) {
        appbar_title.isVisible = visiblility
        appbar_title.text = title

        if (subtitle.isNotEmpty()) {
            appbar_subtitle.isVisible = true
            appbar_subtitle.text = subtitle
        }
    }

    private fun setActionBarScrollFlag() {
        val params = appbar.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED
    }

    private fun setActionBarBack() {
        appbar_back.setOnClickListener { onBackPressed() }
    }
}