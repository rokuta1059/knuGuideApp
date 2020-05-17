package com.knu.knuguide.view

import android.content.Intent
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity

abstract class KNUActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun navigateTo(cls: Class<Any>, @Nullable bundle: Bundle) {
        val intent: Intent = Intent()
        if (bundle != null)
            intent.putExtras(bundle)

        startActivity(intent)
    }

    protected fun navigateToForResult(cls: Class<Any>, @Nullable bundle: Bundle, requestCode: Int) {
        val intent: Intent = Intent()
        if (bundle != null)
            intent.putExtras(bundle)

        startActivityForResult(intent, requestCode)
    }

    protected fun switchTo(cls: Class<Any>, @Nullable bundle: Bundle) {
        val intent: Intent = Intent()
        if (bundle != null)
            intent.putExtras(bundle)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
              .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
              .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    protected fun restartActivity(cls: Class<Any>, @Nullable bundle: Bundle) {
        val intent: Intent = Intent()
        if (bundle != null)
            intent.putExtras(bundle)

        startActivity(intent)
        finish()
    }

    protected fun restartActivityForResult(cls: Class<Any>, @Nullable bundle: Bundle, requestCode: Int) {
        val intent: Intent = Intent()
        if (bundle != null)
            intent.putExtras(bundle)

        startActivityForResult(intent, requestCode)
        finish()
    }
}