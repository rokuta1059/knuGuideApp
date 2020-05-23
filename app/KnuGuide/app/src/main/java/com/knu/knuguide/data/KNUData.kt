package com.knu.knuguide.data

import java.io.Serializable

interface KNUData : Serializable {

    class Type {
        companion object {
            const val ITEM_ANNOUNCEMENT = 10
        }
    }

    fun getRecyclerType(): Int

    fun compare(other: KNUData): Boolean { return false }
}