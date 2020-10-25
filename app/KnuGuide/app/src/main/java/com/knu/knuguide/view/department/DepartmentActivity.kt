package com.knu.knuguide.view.department

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.google.android.material.snackbar.Snackbar
import com.knu.knuguide.R
import com.knu.knuguide.core.KNUService
import com.knu.knuguide.data.department.Credit
import com.knu.knuguide.data.search.Department
import com.knu.knuguide.support.FastClickPreventer
import com.knu.knuguide.view.KNUActivity
import com.knu.knuguide.view.WebViewActivity
import com.knu.knuguide.view.announcement.AnnouncementActivity.Companion.KEY_DEPARTMENT
import com.knu.knuguide.view.search.SearchActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.activity_department.*
import kotlinx.android.synthetic.main.activity_department.tv_college
import kotlinx.android.synthetic.main.item_announcement.*
import kotlinx.android.synthetic.main.knu_appbar.*
import kotlinx.android.synthetic.main.knu_appbar.appbar_search
import kotlinx.android.synthetic.main.table_credit.*

class DepartmentActivity : KNUActivity() {
    private val compositeDisposable = CompositeDisposable()
    private val fastClickPreventer = FastClickPreventer()

    private var departmentId: String? = null

    private var taskCounter: Int = 0

    private var creditsIndex: Int = 0

    private var credits = mutableListOf<Credit>()

    private lateinit var alphaAnimation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_department)

        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_animation)

        getExtras()
        getDepartmentInfo()
        getDepartmentCredit()
    }

    private fun getExtras() {
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey(KEY_DEPARTMENT))
                departmentId = bundle.getString(KEY_DEPARTMENT)
        }
    }

    private fun getDepartmentInfo() {
        if (departmentId.isNullOrEmpty()) {
            Snackbar.make(rootLayout, "일시적으로 정보를 받아올 수 없습니다.", Snackbar.LENGTH_SHORT).show()
            return
        }

        progress_bar.startProgressWithCounter(++taskCounter)

        compositeDisposable.add(KNUService.instance()!!.getDepartmentById(departmentId!!).subscribeWith(object : DisposableSingleObserver<Department>() {
            override fun onSuccess(item: Department) {
                progress_bar.stopProgressWithCounter(--taskCounter)

                tv_college.text = item.college
                tv_name.text = item.department
                tv_phone_value.text = item.callnumber
                tv_pos_value.text = item.location
                tv_site_value.apply {
                    paintFlags = paintFlags.or(Paint.UNDERLINE_TEXT_FLAG)
                    text = item.site

                    setOnClickListener {
                        val args = Bundle()
                        args.putSerializable(WebViewActivity.KEY_DATA, item.site)
                        navigateTo(WebViewActivity::class.java, args)
                    }
                }
                ct_filter_credit.setOnClickListener {
                    if (fastClickPreventer.isClickable()) {
                        creditsIndex++
                        if (creditsIndex >= credits.size) creditsIndex = 0

                        updateCredits()
                        ct_tb_credit.startAnimation(alphaAnimation)
                    }
                }
            }

            override fun onError(e: Throwable) {
                progress_bar.stopProgressWithCounter(--taskCounter)

                e.printStackTrace()
            }
        }))
    }

    private fun getDepartmentCredit() {
        if (departmentId.isNullOrEmpty()) return

        progress_bar.startProgressWithCounter(++taskCounter)

        compositeDisposable.add(KNUService.instance()!!.getDepartmentCredit(departmentId!!).subscribeWith(object : DisposableSingleObserver<List<Credit>>() {
            override fun onSuccess(t: List<Credit>) {
                progress_bar.stopProgressWithCounter(--taskCounter)

                credits = t.sortedWith(compareBy { it.compare() }).toMutableList()

                updateCredits()
            }

            override fun onError(e: Throwable) {
                progress_bar.stopProgressWithCounter(--taskCounter)
                Log.d("Error", e.message)
                e.printStackTrace()
            }
        }))
    }

    private fun updateCredits() {
        if (creditsIndex < 0 || creditsIndex >= credits.size) {
            creditsIndex = 0
            return
        }

        credits[creditsIndex].let {
            tv_basic_value.text = "${it.basic}"
            tv_balanced_value.text = "${it.balanced}"
            tv_specialized_value.text = "${it.specialized}"
            tv_college_value.text = "${it.college}"
            tv_partial_total_culture_value.text = "${it.cultureTotal()}"

            tv_required_value.text = "${it.required}"
            tv_select_value.text = "${it.select}"
            tv_partial_total_min_major_value.text = "${it.majorTotal()}"

            tv_deep_value.text = "${it.deep}"
            tv_total_value.text = "${it.majorWithDeepTotal()}"

            tv_teaching_value.text = "${it.teaching}"
            tv_free_choice_value.text = "${it.freeChoice}"
            tv_all_total_value.text = "${it.allTotal()}"

            tv_filter_credit.text = it.clsString()
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

        // KEYS
        const val KEY_DEPARTMENT_INFO = "DEPARTMENT_INFO"
    }
}
