package com.knu.knuguide.core

import android.os.Build
import android.provider.Settings
import com.knu.knuguide.view.landing.SplashActivity
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader(HEADER_USER_KEY, "Android")
        builder.addHeader(HEADER_DEVICE_KEY, "Android")
        builder.addHeader(HEADER_DEVICEID_KEY, Settings.Secure.getString(SplashActivity.appContext.contentResolver, Settings.Secure.ANDROID_ID))
        builder.addHeader(HEADER_DEVICETYPE_KEY, Build.MANUFACTURER + " " + Build.MODEL)

        return chain.proceed(builder.build())
    }

    companion object {
        private const val HEADER_USER_KEY = "User-Agent"
        private const val HEADER_DEVICE_KEY = "Device-Type"
        private const val HEADER_DEVICEID_KEY = "deviceId"
        private const val HEADER_DEVICETYPE_KEY = "deviceType"
    }
}