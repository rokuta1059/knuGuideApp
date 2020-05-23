package com.knu.knuguide.support

import android.content.res.Resources

class Utils {
    companion object {
        fun dp2px(dp: Float): Float {
            return dp * (Resources.getSystem().displayMetrics.densityDpi / 160f)
        }
    }
}