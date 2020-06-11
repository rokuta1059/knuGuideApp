package com.knu.knuguide.data.announcement

import com.google.gson.annotations.SerializedName
import com.knu.knuguide.data.KNUData
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class Announcement(var department: String, var title: String) : Serializable, KNUData {

    var type = Type.PREVIEW // Default PREVIEW
    var isFavorite = false

    lateinit var date: Date

    override fun getRecyclerType(): Int {
        return when (type) {
            Type.PREVIEW -> KNUData.Type.ITEM_ANNOUNCEMENT_PREVIEW
            Type.GENERAL -> KNUData.Type.ITEM_ANNOUNCEMENT
        }
    }

    fun getDateString(): String {
        if (date != null) {
            return SimpleDateFormat("yyyy-MM-dd").format(date)
        }

        return "오류"
    }

    enum class Type {
        PREVIEW, GENERAL
    }
}