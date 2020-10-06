package com.knu.knuguide.view.bus

import android.os.Bundle
import com.knu.knuguide.R
import com.knu.knuguide.view.KNUActivity

class BusActivity : KNUActivity() {
    override fun getKNUID(): String {
        return KNU_ID
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus)

    }

    companion object {
        const val KNU_ID = "BusActivity"
    }
}