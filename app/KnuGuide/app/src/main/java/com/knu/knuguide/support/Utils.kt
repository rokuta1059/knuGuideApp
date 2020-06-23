package com.knu.knuguide.support

import android.content.res.Resources
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.knu.knuguide.R
import java.util.*

class Utils {
    companion object {
        fun dp2px(dp: Float): Float {
            return dp * (Resources.getSystem().displayMetrics.densityDpi / 160f)
        }

        fun getDate(year: Int, month: Int, day: Int): GregorianCalendar {
            return GregorianCalendar(year, month, day)
        }

        fun showSnackbar(view: View, message: String) {
            try {
                if (TextUtils.isEmpty(message)) {
                    return
                }
                val snackbar: Snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                snackbar.view.setOnClickListener { snackbar.dismiss() }
                snackbar.view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.appBackground))
                val mainTextView: TextView = snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text)
                val actionTextView: TextView = snackbar.view.findViewById(com.google.android.material.R.id.snackbar_action)
                mainTextView.setTextColor(Color.WHITE)
                actionTextView.setTextColor(Color.WHITE)
                snackbar.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}