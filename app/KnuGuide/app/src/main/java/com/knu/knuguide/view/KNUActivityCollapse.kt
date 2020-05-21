package com.knu.knuguide.view

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
            MainActivity.KNU_ID -> {
                setSupportActionBar(toolbar)
            }
        }
    }

    private fun updateActionBar(KNU_ID: String) {
        when (KNU_ID) {
            MainActivity.KNU_ID -> {
                setActionBarTitle(false, "", "")
            }
        }
    }

    private fun setActionBarTitle(visiblility: Boolean, title: String, subtitle: String) {
        if (!visiblility) {
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }
}