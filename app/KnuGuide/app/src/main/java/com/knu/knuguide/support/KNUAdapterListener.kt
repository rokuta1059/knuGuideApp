package com.knu.knuguide.support

import com.knu.knuguide.data.calendar.Task
import com.knu.knuguide.data.search.Department

interface KNUAdapterListener {
    // TODO : 구현
    fun onCalendarTaskItemClick(task: Task, isUnchecked: Boolean): Boolean { return false }

    fun onSearchItemClick(item: Department) {}
}