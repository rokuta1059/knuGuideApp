package com.knu.knuguide.view.main

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.knu.knuguide.R
import com.knu.knuguide.core.KNUService
import com.knu.knuguide.data.User
import com.knu.knuguide.view.KNUActivity
import com.orhanobut.logger.Logger
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.subscribers.DefaultSubscriber
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody

class MainActivity : KNUActivity() {
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchStation.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if ( event?.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    getStations(searchStation.text.toString())
                    return true
                }
                return false
            }
        })
    }

    fun getStations(stationName: String) {
        compositeDisposable.add(KNUService.instance()!!.postStation(stationName)
            .subscribeWith(object : DisposableSingleObserver<ArrayList<String>>() {
                override fun onSuccess(t: ArrayList<String>) {
                    station.text = ""

                    for (name in t) {
                        station.append(name + "\n")
                    }
                }
                override fun onError(e: Throwable) {
                    Logger.d("onError ${e.message}")
                }
            }))
    }
}
