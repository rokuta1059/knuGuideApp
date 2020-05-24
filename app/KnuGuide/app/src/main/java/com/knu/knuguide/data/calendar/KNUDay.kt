package com.knu.knuguide.data.calendar

import com.knu.knuguide.data.KNUData
import java.io.Serializable

class KNUDay(private val type: Int, var day: Int, var isWeekEnd: Boolean) : Serializable, KNUData {

    private var task = ArrayList<Task>()

    fun getTask(): ArrayList<Task> = task

    fun addTask(t: Task) {
        task.add(t)
    }

    override fun getRecyclerType(): Int {
        return type
    }

    override fun compare(other: KNUData): Boolean {
        return super.compare(other)
    }
}