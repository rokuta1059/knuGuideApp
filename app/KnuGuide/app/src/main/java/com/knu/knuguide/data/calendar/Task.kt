package com.knu.knuguide.data.calendar

import android.graphics.Color
import com.knu.knuguide.data.KNUData
import java.io.Serializable
import java.util.*

class Task : Serializable, KNUData {
    private var start: GregorianCalendar
    private var end: GregorianCalendar? = null
    var content: String

    constructor(start: GregorianCalendar, content: String) {
        this.start = start
        this.content = content
    }
    constructor(start: GregorianCalendar, end: GregorianCalendar, content: String) {
        this.start = start
        this.end = end
        this.content = content
    }

    override fun getRecyclerType(): Int {
        return KNUData.Type.ITEM_TASK
    }

    fun getDateRangeText(): String {
        val startText = "${start.get(Calendar.YEAR)}.${getZeroFormat(start.get(Calendar.MONTH))}.${getZeroFormat(start.get(Calendar.DAY_OF_MONTH))}"

        return if (end == null) { startText }
        else {
            val endText = "${end!!.get(Calendar.YEAR)}.${getZeroFormat(end!!.get(Calendar.MONTH))}.${getZeroFormat(end!!.get(Calendar.DAY_OF_MONTH))}"

            "$startText ~ $endText"
        }
    }

    private fun getZeroFormat(value: Int): String = if (value < 10) "0$value" else "$value"

    fun getStartDay(current: Int): Int {
        return if (start.get(Calendar.MONTH) != current) {
            1
        } else {
            start.get(Calendar.DAY_OF_MONTH)
        }
    }

    fun getEndDay(current: Int): Int {
        return when {
            end == null -> {
                -1
            }
            end!!.get(Calendar.MONTH) != current -> {
                end!!.getActualMaximum(Calendar.DAY_OF_MONTH)
            }
            else -> {
                end!!.get(Calendar.DAY_OF_MONTH)
            }
        }
    }
}