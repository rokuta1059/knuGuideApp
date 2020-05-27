package com.knu.knuguide.data.calendar

import com.knu.knuguide.data.KNUData
import java.io.Serializable

class KNUDay(private val type: Int, var day: Int, var isWeekEnd: Boolean) : Serializable, KNUData {
    var includeInSchedule = false

    override fun getRecyclerType(): Int {
        return type
    }

    override fun compare(other: KNUData): Boolean {
        return super.compare(other)
    }
}