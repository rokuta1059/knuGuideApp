package com.knu.knuguide.support

import com.knu.knuguide.data.calendar.Task

interface KNUAdapterListener {
    // TODO : 구현
    fun onCalendarTaskItemClick(task: Task, isUnchecked: Boolean): Boolean { return false }
}