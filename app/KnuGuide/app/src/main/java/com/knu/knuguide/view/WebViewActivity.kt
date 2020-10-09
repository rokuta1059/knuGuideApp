package com.knu.knuguide.view

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.knu.knuguide.R
import com.knu.knuguide.support.FastClickPreventer
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.knu_appbar.*
import kotlinx.android.synthetic.main.knu_appbar_collapse.*
import kotlinx.android.synthetic.main.knu_appbar_collapse.appbar_back

class WebViewActivity : KNUActivity() {
    private val fastClickPreventer = FastClickPreventer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        // Click Listener
        appbar_back.setOnClickListener { if (fastClickPreventer.isClickable()) onBackPressed() }

        progress_bar.startProgress()

        web_view.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progress_bar.stopProgress()
            }
        }

        try {
            if (savedInstanceState != null) {
                finish()
            } else {
                val bundle = intent.extras
                if (bundle != null) {
                    val data =
                        bundle.getSerializable(WebViewActivity.KEY_DATA) as String?
                    if (!TextUtils.isEmpty(data)) {
                        web_view.loadUrl(data!!)
                    }
                }
            }
        } catch (e: Exception) {
            finish()
        }
    }

    override fun getKNUID(): String {
        return KNU_ID
    }

    companion object {
        const val KNU_ID = "WebViewActivity"

        const val KEY_DATA = "KEY_DATA"
    }
}
