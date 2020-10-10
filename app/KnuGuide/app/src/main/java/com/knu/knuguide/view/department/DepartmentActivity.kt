package com.knu.knuguide.view.department

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.knu.knuguide.R
import com.knu.knuguide.core.KNUService
import com.knu.knuguide.data.search.Department
import com.knu.knuguide.support.FastClickPreventer
import com.knu.knuguide.view.KNUActivity
import com.knu.knuguide.view.announcement.AnnouncementActivity.Companion.KEY_DEPARTMENT
import com.knu.knuguide.view.search.SearchActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.activity_department.*
import kotlinx.android.synthetic.main.knu_appbar.*
import kotlinx.android.synthetic.main.knu_appbar.appbar_search

class DepartmentActivity : KNUActivity() {
    private val compositeDisposable = CompositeDisposable()
    private val fastClickPreventer = FastClickPreventer()

    private var departmentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_department)

        getExtras()

        appbar_search.setOnClickListener {
            if (fastClickPreventer.isClickable()) {
                val bundle = Bundle()
                bundle.putBoolean(KEY_DEPARTMENT_INFO, true)
                navigateToForResult(SearchActivity::class.java, bundle, REQ_CODE_SEARCH_DEPARTMENT)
            }
        }

        getDepartmentInfo()
    }

    private fun getExtras() {
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey(KEY_DEPARTMENT)) departmentId = (bundle.getSerializable(
                KEY_DEPARTMENT
            ) as Department).id
        }
    }

    private fun getDepartmentInfo() {
        if (departmentId.isNullOrEmpty()) {
            Snackbar.make(rootLayout, "일시적으로 정보를 받아올 수 없습니다.", Snackbar.LENGTH_SHORT).show()
            return
        }

        progress_bar.startProgress()

        compositeDisposable.add(KNUService.instance()!!.getDepartmentById(departmentId!!).subscribeWith(object : DisposableSingleObserver<Department>() {
            override fun onSuccess(item: Department) {
                progress_bar.stopProgress()

                tv_college.text = item.college
                tv_name.text = item.department
                tv_phone_value.text = item.callnumber
                tv_pos_value.text = item.location
                tv_site_value.text = item.site
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        }))
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun getKNUID(): String {
        return KNU_ID
    }

    companion object {
        const val KNU_ID = "DepartmentActivity"

        // REQ_CODE
        const val REQ_CODE_SEARCH_DEPARTMENT = 200

        // KEYS
        const val KEY_DEPARTMENT_INFO = "DEPARTMENT_INFO"
    }
}
