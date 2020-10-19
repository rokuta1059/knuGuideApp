package com.knu.knuguide.data.department

import com.google.gson.annotations.SerializedName

data class Credit (
    @SerializedName("id")
    var id: String?,
    @SerializedName("year")
    var year: Int,
    @SerializedName("classification")
    var cls: String?,
    @SerializedName("totalcredit")
    var totalCredit: Int,
    @SerializedName("basic")
    var basic: Int,
    @SerializedName("balanced")
    var balanced: Int,
    @SerializedName("specialized")
    var specialized: Int,
    @SerializedName("college")
    var college: Int,
    @SerializedName("required")
    var required: Int,
    @SerializedName("select")
    var select: Int,
    @SerializedName("deep")
    var deep: Int,
    @SerializedName("teaching")
    var teaching: Int,
    @SerializedName("freechoice")
    var freeChoice: Int
) {
    fun compare(): Int {
        return when (cls) {
            "single" -> 0
            "double" -> 1
            "minor" -> 2
            else -> 4
        }
    }

    fun clsString(): String {
        return when (cls) {
            "single" -> "주전공"
            "double" -> "복수전공"
            "minor" -> "부전공"
            else -> ""
        }
    }

    fun cultureTotal(): Int = basic + balanced + specialized + college
    fun majorTotal(): Int = required + select
    fun majorWithDeepTotal(): Int = majorTotal() + deep
    fun allTotal(): Int = cultureTotal() + majorWithDeepTotal() + teaching + freeChoice
}