package com.knu.knuguide.view.main

import android.os.Bundle
import com.knu.knuguide.R
import com.knu.knuguide.view.KNUActivity

class MainActivity : KNUActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
