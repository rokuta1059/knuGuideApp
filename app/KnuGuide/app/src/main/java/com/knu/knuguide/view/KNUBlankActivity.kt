package com.knu.knuguide.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.knu.knuguide.core.KNUService

abstract class KNUBlankActivity : AppCompatActivity() {
    protected abstract fun getKNUID(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KNUService.instance()
    }

    protected fun navigateTo(cls: Class<*>, bundle: Bundle?) {
        val intent: Intent = Intent(this, cls)
        if (bundle != null)
            intent.putExtras(bundle)

        startActivity(intent)
    }

    protected fun navigateToForResult(cls: Class<*>, bundle: Bundle?, requestCode: Int) {
        val intent: Intent = Intent(this, cls)
        if (bundle != null)
            intent.putExtras(bundle)

        startActivityForResult(intent, requestCode)
    }

    protected fun switchTo(cls: Class<*>, bundle: Bundle?) {
        val intent: Intent = Intent(this, cls)
        if (bundle != null)
            intent.putExtras(bundle)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    protected fun restartActivity(cls: Class<*>, bundle: Bundle?) {
        val intent: Intent = Intent(this, cls)
        if (bundle != null)
            intent.putExtras(bundle)

        startActivity(intent)
        finish()
    }

    protected fun restartActivityForResult(cls: Class<*>, bundle: Bundle?, requestCode: Int) {
        val intent: Intent = Intent(this, cls)
        if (bundle != null)
            intent.putExtras(bundle)

        startActivityForResult(intent, requestCode)
        finish()
    }
}