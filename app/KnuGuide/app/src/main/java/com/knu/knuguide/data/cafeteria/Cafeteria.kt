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

    var type = Type.JAEJEONG

    override fun getRecyclerType(): Int {
        return when(type){
            Type.JAEJEONG -> KNUData.Type.ITEM_CAFETERIA_JAEJEONG
            Type.SAEROM -> KNUData.Type.ITEM_CAFETERIA_SAEROM
            Type.IRUM -> KNUData.Type.ITEM_CAFETERIA_IRUM
        }
    }

    enum class Type{
        JAEJEONG, SAEROM, IRUM
    }
}