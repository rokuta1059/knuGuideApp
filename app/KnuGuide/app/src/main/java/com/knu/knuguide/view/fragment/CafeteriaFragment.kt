package com.knu.knuguide.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.knu.knuguide.R
import com.knu.knuguide.core.KNUService
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.cafeteria.Cafeteria
import com.knu.knuguide.data.cafeteria.Menu
import com.knu.knuguide.data.cafeteria.MenuType
import com.knu.knuguide.support.KNUAdapterListener
import com.knu.knuguide.view.adapter.CafeteriaAdapter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.fragment_cafeteria.*
import kotlinx.android.synthetic.main.knu_appbar.*
import java.util.*

class CafeteriaFragment(
    val position: Int,
    val title: String): Fragment(), KNUAdapterListener {

    // Required when protect to Orientation Change is occurred Exception
    init {
        retainInstance = true
    }

    private val items = mutableListOf<KNUData>()

    private val calendar = GregorianCalendar()

    private var dayOfWeek: Int = convertDayOfWeekIndex(calendar.get(Calendar.DAY_OF_WEEK))
    private var dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    private var month = calendar.get(Calendar.MONTH) + 1

    private val compositeDisposable = CompositeDisposable()

    private lateinit var cafeteriaAdapter: CafeteriaAdapter

    private lateinit var alphaAnimation: Animation

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cafeteria, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            alphaAnimation = AnimationUtils.loadAnimation(it, R.anim.alpha_animation)
        }

        setUpRecyclerView()
        getCafeteriaList(title)

        bt_prev_day.setOnClickListener {
            decrementDay()
            updateView()
            updateCafeteriaAdapter()
        }

        bt_next_day.setOnClickListener {
            incrementDay()
            updateView()
            updateCafeteriaAdapter()
        }

        tv_title.text = "${title}생활관"
        updateView()
    }

    private fun setUpRecyclerView() {
        context?.let {
            cafeteriaAdapter = CafeteriaAdapter(it, this)

            recyclerView_cafeteria.layoutManager = LinearLayoutManager(it)
            recyclerView_cafeteria.adapter = cafeteriaAdapter
        }
    }

    private fun getCafeteriaList(id: String){
        cafeteriaAdapter?.let {

            compositeDisposable.add(KNUService.instance()!!.getCafeteriaById(id).subscribeWith(object : DisposableSingleObserver<List<Cafeteria>>() {
                override fun onSuccess(list: List<Cafeteria>) {
                    if (items.isNotEmpty()) items.clear()

                    items.addAll(list)

                    updateCafeteriaAdapter()
                }

                override fun onError(e: Throwable) {
                    Snackbar.make(rootLayout, "네트워크 에러", Snackbar.LENGTH_SHORT).show()
                    Log.d("Error", e.message!!)
                }
            }))
        }
    }

    private fun incrementDay() {
        dayOfWeek++
        dayOfMonth++

        if (dayOfMonth > calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            if (month == 12) {
                calendar[Calendar.MONTH] = 0
                calendar[Calendar.YEAR] += 1
                month = 1
            }
            else {
                calendar[Calendar.MONTH] = month
                month++
            }

            dayOfMonth = 1
        }
    }

    private fun decrementDay() {
        dayOfWeek--
        dayOfMonth--

        if (dayOfMonth == 0) {
            if (month == 1) {
                calendar[Calendar.MONTH] = 11
                calendar[Calendar.YEAR] -= 1
                month = 12
            }
            else {
                month--
                calendar[Calendar.MONTH] = month - 1
            }

            dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        }
    }

    private fun dateToString(): String {
        val dayOfWeekString = when (dayOfWeek) {
            0 -> "월"
            1 -> "화"
            2 -> "수"
            3 -> "목"
            4 -> "금"
            5 -> "토"
            6 -> "일"
            else -> ""
        }
        return "${month}월 ${dayOfMonth}일 ${dayOfWeekString}요일"
    }

    private fun convertDayOfWeekIndex(dayOfWeek: Int): Int {
        return if (dayOfWeek - 2 < 0) 6
        else dayOfWeek - 2
    }

    private fun updateCafeteriaAdapter() {
        if (dayOfWeek < items.size) {
            cafeteriaAdapter.replace((items[dayOfWeek] as Cafeteria).menus)
        }
        else cafeteriaAdapter.clear()

        if (alphaAnimation != null) {
            recyclerView_cafeteria.startAnimation(alphaAnimation)
        }
    }

    private fun updateView() {
        tv_date.text = dateToString()

        bt_next_day.visibility = if (dayOfWeek < 6) View.VISIBLE else View.INVISIBLE
        bt_prev_day.visibility = if (dayOfWeek > 0) View.VISIBLE else View.INVISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}