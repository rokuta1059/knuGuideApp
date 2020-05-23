package com.knu.knuguide.data.announcement

import com.google.gson.annotations.SerializedName
import com.knu.knuguide.data.KNUData
import java.io.Serializable

class Announcement(var source: String, var title: String) : Serializable, KNUData {


    override fun getRecyclerType(): Int {
        return KNUData.Type.ITEM_ANNOUNCEMENT
    }
}