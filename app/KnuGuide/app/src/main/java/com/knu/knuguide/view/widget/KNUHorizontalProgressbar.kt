package com.knu.knuguide.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.knu.knuguide.R

class KNUHorizontalProgressbar(context: Context, attrs: AttributeSet): FrameLayout(context, attrs) {
    var progressBar: ProgressBar
        private set

    init {
        inflate(context, R.layout.knu_progressbar_horizontal, this)
        progressBar = findViewById(R.id.knu_progressbar_horizontal)
    }

    fun startProgress() {
        progressBar.visibility = View.VISIBLE
    }

    fun stopProgress() {
        progressBar.visibility = View.INVISIBLE
    }

    fun startProgressWithCounter(taskCounter: Int) {
        if (taskCounter == 1) startProgress()
    }

    fun stopProgressWithCounter(taskCounter: Int) {
        if (taskCounter == 0) stopProgress()
    }
}