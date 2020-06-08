package com.knu.knuguide.view.calendar

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.knu.knuguide.R
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.calendar.DayPosition
import com.knu.knuguide.data.calendar.DayType
import com.knu.knuguide.data.calendar.KNUDay
import com.knu.knuguide.data.calendar.Task
import com.knu.knuguide.support.FastClickPreventer
import com.knu.knuguide.support.KNUAdapterListener
import com.knu.knuguide.support.Utils
import com.knu.knuguide.view.KNUActivity
import com.knu.knuguide.view.adapter.CalendarAdapter
import com.knu.knuguide.view.adapter.CalendarTaskAdapter
import com.knu.knuguide.view.adapter.decor.PreviewAnnouncementDecor
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.cal_contents.*
import kotlinx.android.synthetic.main.cal_header.*
import kotlinx.android.synthetic.main.cal_months.*
import kotlinx.android.synthetic.main.knu_appbar.*
import java.util.*
import kotlin.collections.ArrayList

class CalendarActivity : KNUActivity(), KNUAdapterListener {
    private val faskClickPreventer = FastClickPreventer()

    // 1월 ~ 12월까지 day 정보를 저장할 Map Collection
    var cal_maps = HashMap<Int, ArrayList<KNUData>>()
    var task_maps = HashMap<String, ArrayList<KNUData>>()

    var calendar = GregorianCalendar()

    lateinit var adapter: CalendarAdapter
    lateinit var taskAdapter: CalendarTaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        appbar_back.setOnClickListener{ if (faskClickPreventer.isClickable()) onBackPressed() }

        setTasks()

        initCalHeaders(calendar.get(Calendar.YEAR))
        initCalMaps(calendar.get(Calendar.YEAR))

        adapter = CalendarAdapter(this, cal_maps[1]!!, this) // Default: 1월 달력
        rv_calendar.layoutManager = StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL) // 요일 수만큼
        rv_calendar.adapter = adapter

        taskAdapter = CalendarTaskAdapter(this, task_maps["20205"]!!, this)
        rv_task.layoutManager = LinearLayoutManager(this)
        rv_task.adapter = taskAdapter
        rv_task.addItemDecoration(PreviewAnnouncementDecor(this, 1F))

        tab_months.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
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
        tab_months.getTabAt(calendar.get(Calendar.MONTH))?.select()

        bt_year_prev.setOnClickListener {
            if (faskClickPreventer.isClickable()) {
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1)
                initCalHeaders(calendar.get(Calendar.YEAR))
                initCalMaps(calendar.get(Calendar.YEAR))
                adapter.setItems(cal_maps[tab_months.selectedTabPosition + 1]!!)
                adapter.notifyDataSetChanged()
            }
        }

        bt_year_next.setOnClickListener {
            if (faskClickPreventer.isClickable()) {
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1)
                initCalHeaders(calendar.get(Calendar.YEAR))
                initCalMaps(calendar.get(Calendar.YEAR))
                adapter.setItems(cal_maps[tab_months.selectedTabPosition + 1]!!)
                adapter.notifyDataSetChanged()
            }
        }
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
                dayList.add(KNUDay(KNUData.Type.ITEM_DAY, j))
            }

            cal_maps[i] = dayList
        }
    }

    private fun setTasks() {
        val taskList = ArrayList<KNUData>()

        taskList.add(Task(Utils.getDate(2020, 4, 20), Utils.getDate(2020, 5, 4), "석사학위청구 논문심사 신청(대학원)"))
        taskList.add(Task(Utils.getDate(2020, 5, 4), Utils.getDate(2020,5,8), "1학기 중간 수업평가"))
        taskList.add(Task(Utils.getDate(2020, 5, 8), "중간시험 종료일"))
        taskList.add(Task(Utils.getDate(2020, 5, 20), Utils.getDate(2020,5,22), "계절학기 수강신청"))

        task_maps["20205"] = taskList
    }

    override fun onCalendarTaskItemClick(task: Task, isUnchecked: Boolean): Boolean {
        if (!faskClickPreventer.isClickable())
            return false

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
        adapter.notifyDataSetChanged()

        return true
    }

    override fun getKNUID(): String {
        return KNU_ID
    }

    companion object {
        const val KNU_ID = "CalendarActivity"
    }
}
