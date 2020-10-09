package com.knu.knuguide.support

import android.content.res.Resources
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.knu.knuguide.R
import java.util.*
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

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

        private fun deg2rad(deg: Double): Double = deg * Math.PI / 180.0

        private fun rad2deg(rad: Double): Double = rad * 180 / Math.PI

        fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val theta = abs(lon1 - lon2)
            var dist = sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * cos(deg2rad(theta))

            dist = acos(dist)
            dist = rad2deg(dist)
            dist *= 60 * 1.1515

            return dist * 1609.344
        }
    }
}