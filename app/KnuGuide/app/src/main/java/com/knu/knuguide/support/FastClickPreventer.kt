package com.knu.knuguide.support

class FastClickPreventer {
    private var lastClick: Long = 0L

    init {
        lastClick = 0L
    }

    fun isClickable(): Boolean {
        if (System.currentTimeMillis() - lastClick > SECOND_TO_PREVENT * 1000) {
            lastClick = System.currentTimeMillis()
            return true
        }
        return false
    }

    companion object {
        const val SECOND_TO_PREVENT: Double = 0.5;
    }
}