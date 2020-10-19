package com.knu.knuguide.data.announcement

import com.google.gson.annotations.SerializedName
import com.knu.knuguide.data.KNUData
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class Announcement : Serializable, KNUData {
    @SerializedName("id")
    var id: String? = null
    @SerializedName("departmentid")
    var departmentId: String? = null
    @SerializedName("department_name")
    var department: String? = null
    @SerializedName("announce")
    var number: String? = null
    @SerializedName("name")
    var title: String? = null
    @SerializedName("date")
    var date: String? = null
    @SerializedName("url")
    var link: String? = null

    var type = Type.PREVIEW // Default PREVIEW

    fun isFixed(): Boolean {
        if (number.isNullOrEmpty())
            return false

        return number == "공지"
    }

    override fun getRecyclerType(): Int {
        return when (type) {
            Type.PREVIEW -> KNUData.Type.ITEM_ANNOUNCEMENT_PREVIEW
            Type.GENERAL -> KNUData.Type.ITEM_ANNOUNCEMENT
        }
    }

    enum class Type {
        PREVIEW, GENERAL
    }
}