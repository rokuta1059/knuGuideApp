package com.knu.knuguide.view.landing

import android.content.Context
import android.os.Bundle
import android.os.Handler
import com.knu.knuguide.R
import com.knu.knuguide.core.PrefService
import com.knu.knuguide.view.KNUActivity
import com.knu.knuguide.view.main.MainActivity

class SplashActivity : KNUActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        appContext = applicationContext
        PrefService.instance()

        // 3초 후 화면 전환
        Handler().postDelayed({
            switchTo(MainActivity::class.java, null)
        }, 3000)
    }

    override fun getKNUID(): String {
        return KNU_ID
    }

    companion object {
        const val KNU_ID = "SplashActivity"
        lateinit var appContext: Context
    }
}
