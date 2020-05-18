package com.knu.knuguide.view.landing

import android.os.Bundle
import android.os.Handler
import com.knu.knuguide.R
import com.knu.knuguide.view.KNUActivity
import com.knu.knuguide.view.main.MainActivity

class SplashActivity : KNUActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 3초 후 화면 전환
        Handler().postDelayed({
            switchTo(MainActivity::class.java, null)
        }, 3000)
    }
}
