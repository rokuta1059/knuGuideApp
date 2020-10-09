package com.knu.knuguide.data

import androidx.annotation.LayoutRes
import java.io.Serializable

interface KNUData : Serializable {

    class Type {
        companion object {
            const val ITEM_ANNOUNCEMENT_PREVIEW = 10
            const val ITEM_ANNOUNCEMENT = 11

            // Calendar
            const val ITEM_DAY_EMPTY = 20
            const val ITEM_DAY = 21
            const val ITEM_TASK = 22

            // Search
            const val ITEM_SEARCH_DEPARTMENT = 30

            // Bus
            const val ITEM_BUS_STOP = 100

            // Bus Info
            const val ITEM_BUS_ROUTE = 110
            const val ITEM_BUS_ROUTE_HEADER = 111
            const val ITEM_BUS_ROUTE_INTERVAL = 112

        }
    }

    fun getRecyclerType(): Int

    fun compare(other: KNUData): Boolean = false
}