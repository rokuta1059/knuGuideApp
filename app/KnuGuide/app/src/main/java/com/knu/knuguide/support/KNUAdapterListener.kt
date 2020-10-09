package com.knu.knuguide.support

import com.knu.knuguide.data.announcement.Announcement
import com.knu.knuguide.data.bus.BusStop
import com.knu.knuguide.data.calendar.Task
import com.knu.knuguide.data.search.Department

interface KNUAdapterListener {
    // TODO : 구현
    fun onCalendarTaskItemClick(task: Task, isUnchecked: Boolean): Boolean { return false }

    fun onSearchItemClick(item: Department) {}

    fun onAnnouncementClick(item: Announcement) {}

    // BusActivity
    fun onBusStopClicked(item: BusStop) {}

    // BusInfoActivity
    fun scrollToNodePosition(position: Int) {}
}