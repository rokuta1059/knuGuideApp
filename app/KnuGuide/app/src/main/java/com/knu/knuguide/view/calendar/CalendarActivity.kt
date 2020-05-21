package com.knu.knuguide.view.calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.knu.knuguide.R
import com.knu.knuguide.view.KNUActivityCollapse
import kotlinx.android.synthetic.main.knu_appbar_collapse.*

class CalendarActivity : KNUActivityCollapse() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        back.setOnClickListener{ onBackPressed() }
    }

    override fun getKNUID(): String {
        return KNU_ID
    }

    companion object {
        const val KNU_ID = "CalendarActivity"
    }
}
