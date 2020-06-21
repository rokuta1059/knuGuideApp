package com.knu.knuguide.data

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
        }
    }

    fun getRecyclerType(): Int

    fun compare(other: KNUData): Boolean { return false }
}