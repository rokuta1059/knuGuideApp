package com.knu.knuguide.view

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import com.google.android.material.appbar.AppBarLayout
import com.knu.knuguide.R
import com.knu.knuguide.view.announcement.AnnouncementActivity
import com.knu.knuguide.view.calendar.CalendarActivity
import com.knu.knuguide.view.department.DepartmentActivity
import com.knu.knuguide.view.main.MainActivity
import kotlinx.android.synthetic.main.knu_appbar_collapse.*
import kotlin.math.abs

abstract class KNUActivityCollapse : KNUBlankActivity() {

    private var isActionBarVisible = false
    private var isCollapsingActionBar = true

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        initActionBar(getKNUID())
        updateActionBar(getKNUID())
    }

    private fun initActionBar(KNU_ID: String) {
        when (KNU_ID) {
            AnnouncementActivity.KNU_ID -> {
                setSupportActionBar(appbar)

                if (supportActionBar != null) {
                    val actionBar = supportActionBar!!

                    actionBar.setDisplayShowTitleEnabled(false)
                    collapse_appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appbarLayout, verticalOffset ->
                        if (isCollapsingActionBar) {
                            val maxScroll: Int = appbarLayout.totalScrollRange;
                            val percentage: Float = abs(verticalOffset).toFloat() / maxScroll.toFloat()
                            handleToolbarVisibility(appbar, percentage)
                        }
                    })

                    val layoutParams = collapse_appbar.layoutParams as CoordinatorLayout.LayoutParams
                    val behavior = AppBarLayout.Behavior()
                    behavior.setDragCallback(object: AppBarLayout.Behavior.DragCallback() {
                        override fun canDrag(p0: AppBarLayout): Boolean {
                            return false
                        }
                    })
                    layoutParams.behavior = behavior
                }
            }
        }
    }

    private fun updateActionBar(KNU_ID: String) {
        when (KNU_ID) {
            AnnouncementActivity.KNU_ID -> {
                setActionBarCustomView(KNU_ID)
                setActionBarTitle(true, "공지사항")
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    private fun setActionBarCustomView(KNU_ID: String) {
        when (KNU_ID) {
            AnnouncementActivity.KNU_ID -> {
                appbar_back.isVisible = true
                appbar_title.isVisible = true
                appbar_search.isVisible = true
                appbar_star.isVisible = true
                appbar_search_collapsed.isVisible = true
                department_expanded.isVisible = true
                department_collapsed.isVisible = true
            }
        }
    }

    private fun setActionBarTitle(isCollapsing: Boolean, title: String) {
        appbar_title.text = title

        isCollapsingActionBar = isCollapsing

        if (supportActionBar != null) {
            collapse_appbar.setExpanded(isCollapsing, false)

            if (isCollapsingActionBar) {
                startAppBarAlphaAnimation(expanded_toolbar_container, 0, View.VISIBLE)
                startAppBarAlphaAnimation(appbar, 0, View.GONE)
                appbar.visibility = View.INVISIBLE
            }
            else {
                startAppBarAlphaAnimation(expanded_toolbar_container, 0, View.INVISIBLE)
                startAppBarAlphaAnimation(appbar, 0, View.VISIBLE)
            }
        }
    }

    private fun handleToolbarVisibility(view: View, percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!isActionBarVisible) {
                view.visibility = View.VISIBLE
                startAppBarAlphaAnimation(view, ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                isActionBarVisible = true
            }
        }
        else {
            if (isActionBarVisible) {
                view.visibility = View.INVISIBLE
                startAppBarAlphaAnimation(expanded_toolbar_container, ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                isActionBarVisible = false
            }
        }
    }

    companion object {
        private const val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f
        private const val ALPHA_ANIMATIONS_DURATION = 200L

        fun startAppBarAlphaAnimation(view: View, duration: Long, visibility: Int) {
            val alphaAnimation = if (visibility == View.VISIBLE) AlphaAnimation(0f, 1f) else AlphaAnimation(1f, 0f)
            alphaAnimation.duration = duration

            view.startAnimation(alphaAnimation)
        }
    }
}
