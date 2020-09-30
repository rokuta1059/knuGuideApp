package com.knu.knuguide.data.calendar

import com.google.gson.annotations.SerializedName
import com.knu.knuguide.data.KNUData
import java.io.Serializable

class KNUDay(private val type: Int, var day: Int) : Serializable, KNUData {

    var dayType = DayType.NONE      // 하루인지 기간인지 판별
    var dayPos = DayPosition.IN     // Task 에서 Day 위치
    var isWeekEnd = false   // 주말인지 아닌지

    override fun getRecyclerType(): Int {
        return type
    }

    override fun compare(other: KNUData): Boolean {
        return super.compare(other)
    }
}

/*
    선택된 Task 와의 연결 정보
    1. NONE : 해당 없음
    2. SINGLE : 하루
    3. DURATION : 기간 (하루 초과)
 */
enum class DayType {
    NONE, SINGLE, DURATION
}

/*
    Task 에서 Day 의 위치
    1. START : 시작일
    2. IN : 중간
    3. END : 마지막일
 */
enum class DayPosition {
    START, IN, END
}