package com.knu.knuguide.view.department

import android.content.Intent
import android.os.Bundle
import com.knu.knuguide.R
import com.knu.knuguide.core.KNUService
import com.knu.knuguide.data.search.Department
import com.knu.knuguide.support.FastClickPreventer
import com.knu.knuguide.view.KNUActivity
import com.knu.knuguide.view.announcement.AnnouncementActivity
import com.knu.knuguide.view.search.SearchActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.activity_department.*
import kotlinx.android.synthetic.main.knu_appbar.*
import kotlinx.android.synthetic.main.knu_appbar.appbar_back
import kotlinx.android.synthetic.main.knu_appbar.appbar_search
import kotlinx.android.synthetic.main.knu_appbar_collapse.*
import kotlinx.android.synthetic.main.preview_announcement.*

class DepartmentActivity : KNUActivity() {
    private val compositeDisposable = CompositeDisposable()
    private val fastClickPreventer = FastClickPreventer()

    private var departmentId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_department)

        appbar_back.setOnClickListener { if (fastClickPreventer.isClickable()) onBackPressed() }
        appbar_search.setOnClickListener {
            if (fastClickPreventer.isClickable()) {
                val bundle = Bundle()
                bundle.putBoolean(KEY_DEPARTMENT_INFO, true)
                navigateToForResult(SearchActivity::class.java, bundle, REQ_CODE_SEARCH_DEPARTMENT)
            }
        }

        getDepartmentInfo()
    }

    private fun getDepartmentInfo() {
        if (departmentId.isEmpty()) {
            val bundle = Bundle()
            bundle.putBoolean(KEY_DEPARTMENT_INFO, true)
            navigateToForResult(SearchActivity::class.java, bundle, REQ_CODE_SEARCH_DEPARTMENT)
            return
        }

        progress_bar.startProgress()

        compositeDisposable.add(KNUService.instance()!!.getDepartmentById(departmentId).subscribeWith(object : DisposableSingleObserver<Department>() {
            override fun onSuccess(item: Department) {
                progress_bar.stopProgress()

                college.text = item.college
                department.text = item.department
                number.text = item.callnumber
                location.text = item.location
                link.text = item.site
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        }))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == AnnouncementActivity.REQ_CODE_SEARCH_DEPARTMENT) {
                val item = data?.getSerializableExtra(AnnouncementActivity.KEY_DEPARTMENT) as Department
                if (item != null) {
                    // 검색해서 온거면
                    // 즐겨찾기 상태를 해제
                    departmentId = item.id!!
                    getDepartmentInfo()
                }
            }
        }
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
