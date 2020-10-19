package com.knu.knuguide.data.cafeteria

import com.google.gson.annotations.SerializedName
import com.knu.knuguide.data.KNUData
import java.io.Serializable

class Cafeteria: KNUData, Serializable {

    @SerializedName("date")
    var date: String? = null
    @SerializedName("dormitory")
    var dormitory: String? = null
    @SerializedName("week")
    var week: String? = null
    @SerializedName("breakfast")
    var breakfast: String? = null
    @SerializedName("lunch")
    var lunch: String? = null
    @SerializedName("dinner")
    var dinner: String? = null

    override fun getRecyclerType(): Int = KNUData.Type.ITEM_CAFETERIA
}