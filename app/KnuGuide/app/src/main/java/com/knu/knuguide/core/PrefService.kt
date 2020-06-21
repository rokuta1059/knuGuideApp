package com.knu.knuguide.core

import android.content.Context
import android.content.SharedPreferences
import com.knu.knuguide.view.landing.SplashActivity
import java.util.*

class PrefService {

    private val listeners = HashMap<String, PrefChangeListener>()

    private lateinit var pref: SharedPreferences
    private var editor: SharedPreferences.Editor? = null

    private lateinit var noticeId: String

    interface PrefChangeListener {
        fun onChangePref(key: String, value: Any)
    }

    init {
        val context: Context = SplashActivity.appContext
        pref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

        noticeId = pref.getString(NOTICE_DEPARTMENT_KEY, "")!!
    }

    fun register(key: String, listener: PrefChangeListener?) {
        service!!.listeners[key] = listener!!
    }

    fun unregister(key: String?) {
        service!!.listeners.remove(key)
    }

    private fun broadcast(key: String, value: Any) {
        for (listener in service!!.listeners.values)
            listener.onChangePref(key, value)
    }

    fun getNoticeId(): String {
        return noticeId
    }

    fun putNoticeId(id: String) {
        noticeId = id
        service?.put(NOTICE_DEPARTMENT_KEY, id)
    }

    private fun put(key: String, value: String) {
        doEdit()
        editor?.putString(key, value)
        doCommit()
    }

    private fun put(key: String, value: Int) {
        doEdit()
        editor?.putInt(key, value)
        doCommit()
    }

    private fun put(key: String, value: Boolean) {
        doEdit()
        editor?.putBoolean(key, value)
        doCommit()
    }


    private fun doEdit() {
        if (editor == null) {
            editor = pref.edit()
        }
    }

    private fun doCommit() {
        if (editor != null) {
            editor?.commit()
            editor = null
        }
    }


    companion object {
        private var service: PrefService? = null

        const val NOTICE_DEPARTMENT_KEY = "NOTICE_DEPARTMENT_KEY"

        @Synchronized
        fun instance(): PrefService? {
            if (PrefService.service == null) {
                service = PrefService()
            }
            return service
        }
    }
}