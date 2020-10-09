package com.knu.knuguide.view.calendar

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.knu.knuguide.R
import com.knu.knuguide.core.KNUService
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.calendar.DayPosition
import com.knu.knuguide.data.calendar.DayType
import com.knu.knuguide.data.calendar.KNUDay
import com.knu.knuguide.data.calendar.Task
import com.knu.knuguide.support.FastClickPreventer
import com.knu.knuguide.support.KNUAdapterListener
import com.knu.knuguide.view.KNUActivity
import com.knu.knuguide.view.adapter.CalendarAdapter
import com.knu.knuguide.view.adapter.CalendarTaskAdapter
import com.knu.knuguide.view.adapter.decor.PreviewAnnouncementDecor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.cal_contents.*
import kotlinx.android.synthetic.main.cal_header.*
import kotlinx.android.synthetic.main.cal_months.*
import kotlinx.android.synthetic.main.knu_appbar.*
import java.util.*
import kotlin.collections.ArrayList

class CalendarActivity : KNUActivity(), KNUAdapterListener {
    private val fastClickPreventer = FastClickPreventer()
    private val compositeDisposable = CompositeDisposable()

    // 1월 ~ 12월까지 day 정보를 저장할 Map Collection
    var cal_maps = HashMap<Int, ArrayList<KNUData>>()
    var task_maps = HashMap<String, ArrayList<KNUData>>()

    var calendar = GregorianCalendar()

    lateinit var adapter: CalendarAdapter
    lateinit var taskAdapter: CalendarTaskAdapter

    /**
       todo: 1. 달력 초기화
             2. 일정 데이터 Load & 추가
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        appbar_back.setOnClickListener{ if (fastClickPreventer.isClickable()) onBackPressed() }

        // setUpCalendar
        initCalHeaders(calendar.get(Calendar.YEAR))
        initCalMaps(calendar.get(Calendar.YEAR))

        adapter = CalendarAdapter(this, cal_maps[1]!!, this) // Default: 1월 달력
        rv_calendar.layoutManager = StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL) // 요일 수만큼
        rv_calendar.adapter = adapter

        task_maps["202005"] = ArrayList<KNUData>()
        taskAdapter = CalendarTaskAdapter(this, task_maps["202005"]!!, this)
        rv_task.layoutManager = LinearLayoutManager(this)
        rv_task.adapter = taskAdapter
        rv_task.addItemDecoration(PreviewAnnouncementDecor(this, 1F))

        tab_months.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                taskAdapter.unselect()

                val dayList: ArrayList<KNUDay> = adapter.getItems() as ArrayList<KNUDay>
                dayList.iterator().forEach { it.dayType = DayType.NONE }

                tv_contents_title.text = tab?.text
                adapter.setItems(cal_maps[tab_months.selectedTabPosition + 1]!!)
                adapter.notifyDataSetChanged()

                if (task_maps["${calendar.get(Calendar.YEAR)}${tab_months.selectedTabPosition + 1}"].isNullOrEmpty()) {
                    taskAdapter.setItems(ArrayList<KNUData>())
                }
                else {
                    taskAdapter.setItems(task_maps["${calendar.get(Calendar.YEAR)}${tab_months.selectedTabPosition + 1}"]!!)
                }
                taskAdapter.notifyDataSetChanged()
            }
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
        })

        bt_year_prev.setOnClickListener {
            if (fastClickPreventer.isClickable()) {
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1)
                initCalHeaders(calendar.get(Calendar.YEAR))
                initCalMaps(calendar.get(Calendar.YEAR))
                adapter.setItems(cal_maps[tab_months.selectedTabPosition + 1]!!)
                adapter.notifyDataSetChanged()

                if (task_maps["${calendar.get(Calendar.YEAR)}${tab_months.selectedTabPosition + 1}"].isNullOrEmpty()) {
                    taskAdapter.setItems(ArrayList<KNUData>())
                }
                else {
                    taskAdapter.setItems(task_maps["${calendar.get(Calendar.YEAR)}${tab_months.selectedTabPosition + 1}"]!!)
                }
                taskAdapter.notifyDataSetChanged()
            }
        }

        bt_year_next.setOnClickListener {
            if (fastClickPreventer.isClickable()) {
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1)
                initCalHeaders(calendar.get(Calendar.YEAR))
                initCalMaps(calendar.get(Calendar.YEAR))
                adapter.setItems(cal_maps[tab_months.selectedTabPosition + 1]!!)
                adapter.notifyDataSetChanged()

                if (task_maps["${calendar.get(Calendar.YEAR)}${tab_months.selectedTabPosition + 1}"].isNullOrEmpty()) {
                    taskAdapter.setItems(ArrayList<KNUData>())
                }
                else {
                    taskAdapter.setItems(task_maps["${calendar.get(Calendar.YEAR)}${tab_months.selectedTabPosition + 1}"]!!)
                }
                taskAdapter.notifyDataSetChanged()
            }
        }

        // setUpTask
        setTasks()
    }

    // calendar header 초기화
    private fun initCalHeaders(year: Int) {
        tv_year_title.text = "${year}년 학사일정"
        tv_year_prev.text = "${year-1}년"
        tv_year_next.text = "${year+1}년"
    }

    // cal_maps 초기화
    private fun initCalMaps(year: Int) {
        cal_maps.clear()

        // 1월부터 12월까지
        for (i in 1..12) {
            val dayList = ArrayList<KNUData>()
            val calByMonth = GregorianCalendar(year, i-1, 1, 0, 0, 0)

            val dayOfWeek = calByMonth.get(Calendar.DAY_OF_WEEK) - 1
            val max = calByMonth.getActualMaximum(Calendar.DAY_OF_MONTH)

            for (j in 0 until dayOfWeek) {
                dayList.add(KNUDay(KNUData.Type.ITEM_DAY_EMPTY, 0))
            }
            for (j in 1..max) {
                dayList.add(KNUDay(KNUData.Type.ITEM_DAY, j).apply { if ((dayOfWeek + j) % 7 in 0..1) isWeekEnd = true })
            }

            cal_maps[i] = dayList
        }
    }

    private fun setTasks() {
        progress_bar.startProgress()

        compositeDisposable.add(KNUService.instance()!!.getSchedule().subscribeWith(object : DisposableSingleObserver<List<Task>>() {
            override fun onSuccess(list: List<Task>) {
                progress_bar.stopProgress()

                for (task in list) {
                    val startTag = task.startDateToYearMonthTag()
                    val endTag = task.endDateToYearMonthTag()

                    if (task_maps[startTag] == null)
                        task_maps[startTag] = ArrayList<KNUData>()

                    task_maps[startTag]!!.add(task)

                    if (startTag != endTag) {
                        if (task_maps[endTag] == null)
                            task_maps[endTag] = ArrayList<KNUData>()

                        task_maps[endTag]!!.add(task)
                    }
                }

                tab_months.getTabAt(calendar.get(Calendar.MONTH))?.select()
            }

            override fun onError(e: Throwable) {
                progress_bar.stopProgress()
                Snackbar.make(ctl_calendar, "잠시 후 다시 시도해주세요.", Snackbar.LENGTH_SHORT).show()

                Log.d("Error", e.message!!)
            }
        }))
    }

    override fun onCalendarTaskItemClick(task: Task, isUnchecked: Boolean): Boolean {
        /* 현재 월의 Day 정보 초기화 */
        val dayList: ArrayList<KNUDay> = adapter.getItems() as ArrayList<KNUDay>
        dayList.iterator().forEach { it.dayType = DayType.NONE }

        if (!isUnchecked) {
            val startDay = task.getStartDay(tab_months.selectedTabPosition + 1)
            val endDay = task.getEndDay(tab_months.selectedTabPosition + 1)

            var count = 0
            while (dayList[count].getRecyclerType() == KNUData.Type.ITEM_DAY_EMPTY)
                count++

            if (endDay == -1) {
                dayList[count + startDay - 1].dayType = DayType.SINGLE
            }
            else {
                if (startDay == task.getStartDateCalendar().getActualMaximum(Calendar.DAY_OF_MONTH))
                    dayList[count + startDay - 1].dayType = DayType.SINGLE
                else {
                    for (range in startDay..endDay) {
                        dayList[count + range - 1].dayType = DayType.DURATION
                        dayList[count + range - 1].dayPos = DayPosition.IN

                        if (range == startDay)
                            dayList[count + range - 1].dayPos = DayPosition.START
                        if (range == endDay)
                            dayList[count + range - 1].dayPos = DayPosition.END
                    }
                }
            }
        }
        adapter.notifyDataSetChanged()

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()   // 해제
    }

    override fun getKNUID(): String {
        return KNU_ID
    }

    companion object {
        const val KNU_ID = "CalendarActivity"
    }
}
