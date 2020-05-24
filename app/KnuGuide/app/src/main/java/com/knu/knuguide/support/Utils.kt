package com.knu.knuguide.support

import android.content.res.Resources
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        fun dp2px(dp: Float): Float {
            return dp * (Resources.getSystem().displayMetrics.densityDpi / 160f)
        }

        fun getDate(year: Int, month: Int, day: Int): GregorianCalendar {
            return GregorianCalendar(year, month, day)
        }
    }
}