package com.knu.knuguide.data.search

import com.google.gson.annotations.SerializedName
import com.knu.knuguide.data.KNUData
import java.io.Serializable

class Department : KNUData, Serializable {
    @SerializedName("id")
    var id: String? = null
    @SerializedName("college")
    var college: String? = null
    @SerializedName("name")
    var department: String? = null
    @SerializedName("callnumber")
    var callnumber: String? = null
    @SerializedName("location")
    var location: String? = null
    @SerializedName("mapAddress")
    var map: String? = null
    @SerializedName("site")
    var site: String? = null
    @SerializedName("siteAppend")
    var siteAppend: String? = null
    @SerializedName("crf")
    var crf: String? = null

    var isFavorite = false

    override fun getRecyclerType(): Int {
        return KNUData.Type.ITEM_SEARCH_DEPARTMENT
    }

    override fun compare(other: KNUData): Boolean {
        return super.compare(other)
    }
}