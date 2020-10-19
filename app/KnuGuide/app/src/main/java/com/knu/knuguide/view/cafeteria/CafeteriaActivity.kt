package com.knu.knuguide.view.cafeteria

import android.os.Bundle
import com.knu.knuguide.R
import com.knu.knuguide.support.KNUAdapterListener
import com.knu.knuguide.view.KNUActivity
import io.reactivex.disposables.CompositeDisposable

class CafeteriaActivity : KNUActivity(), KNUAdapterListener {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafeteria)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun getKNUID(): String = KNU_ID

    companion object {
        const val KNU_ID = "CafeteriaActivity"
    }
}