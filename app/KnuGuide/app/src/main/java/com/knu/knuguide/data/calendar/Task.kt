package com.knu.knuguide.data.calendar

import android.graphics.Color
import com.google.gson.annotations.SerializedName
import com.knu.knuguide.data.KNUData
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class Task : Serializable, KNUData {
    /**
     * startDate : 시작일
     * endDate : 종료일
     * content : 일정 내용
     */
    @SerializedName("id")
    internal var id: Int = 0
    @SerializedName("startdate")
    internal var startDate: String? = null
    @SerializedName("enddate")
    internal var endDate: String? = null
    @SerializedName("description")
    var content: String? = null

    override fun getRecyclerType(): Int {
        return KNUData.Type.ITEM_TASK
    }

    private fun getStartDate(): String {
        if (startDate.isNullOrEmpty())
            startDate = "2000-01-01"

        return "$startDate"
    }

    private fun getEndDate(): String {
        if (endDate.isNullOrEmpty())
            endDate = "2000-01-01"

        return "$endDate"
    }

    fun getStartDateCalendar(): Calendar {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        // Calendar 시간 설정
        calendar.time = dateFormat.parse(getStartDate())

        return calendar
    }

    fun getEndDateCalendar(): Calendar {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        // Calendar 시간 설정
        calendar.time = dateFormat.parse(getEndDate())

        return calendar
    }

    fun getDateString(): String {
        return if (getStartDate() == getEndDate()) getStartDate()
               else "${getStartDate()} ~ ${getEndDate()}"
    }

    fun startDateToYearMonthTag(): String {
        val calendar = getStartDateCalendar()

        return "${calendar.get(Calendar.YEAR)}${calendar.get(Calendar.MONTH)+1}"
    }

    fun endDateToYearMonthTag(): String {
        val calendar = getEndDateCalendar()

        return "${calendar.get(Calendar.YEAR)}${calendar.get(Calendar.MONTH)+1}"
    }

    fun getStartDay(current: Int): Int {
        val start = getStartDateCalendar()

        return if (start.get(Calendar.MONTH) != current - 1) {
            1
        } else {
            start.get(Calendar.DAY_OF_MONTH)
        }
    }

    fun getEndDay(current: Int): Int {
        val start = getStartDateCalendar()
        val end = getEndDateCalendar()

        return when {
            endDate == startDate -> {
                -1
            }
            end!!.get(Calendar.MONTH) != current - 1 -> {
                start!!.getActualMaximum(Calendar.DAY_OF_MONTH)
            }
            else -> {
                end!!.get(Calendar.DAY_OF_MONTH)
            }
        }
    }
}