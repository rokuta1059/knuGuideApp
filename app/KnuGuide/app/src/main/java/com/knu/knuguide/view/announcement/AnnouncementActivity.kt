package com.knu.knuguide.view.announcement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.core.view.get
import androidx.core.view.size
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.knu.knuguide.R
import com.knu.knuguide.view.KNUActivityCollapse
import kotlinx.android.synthetic.main.activity_announcement.*
import kotlinx.android.synthetic.main.announcement_department.*
import kotlinx.android.synthetic.main.knu_appbar_collapse.*

class AnnouncementActivity : KNUActivityCollapse() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcement)

        appbar_back.setOnClickListener { onBackPressed() }
    }

    override fun getKNUID(): String {
        return KNU_ID
    }

    companion object {
        const val KNU_ID = "AnnouncementActivity"
    }
}
