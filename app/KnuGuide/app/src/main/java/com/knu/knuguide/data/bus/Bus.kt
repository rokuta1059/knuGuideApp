package com.knu.knuguide.data.bus

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(strict = false, name = "response")
data class Bus (
    @field:Element(name = "header")
    var header: Header? = null,
    @field:Element(name = "body")
    var body: BusBody? = null
)

@Root(strict = false, name = "body")
data class BusBody(
    @field:ElementList(name = "items")
    var items: MutableList<BusItem> = mutableListOf(),
    @field:Element(name = "totalCount")
    var totalCount: Int = 0
)

@Root(strict = false, name = "item")
data class BusItem(
    @field:Element(name = "arrprevstationcnt")
    var arrPrevStationCnt: Int = 0,
    @field:Element(name = "arrtime")
    var arrTime: Int = 0,
    @field:Element(name = "nodeid")
    var nodeId: String = "",
    @field:Element(name = "nodenm")
    var nodeName: String = "",
    @field:Element(name = "routeid")
    var routeId: String = "",
    @field:Element(name = "routeno")
    var routeNumber: Int = 0
) {
    fun getArrTimeToString(): String = "약 ${arrTime / 60}분"
    fun getArrPrevStationCntToString(): String = "${arrPrevStationCnt}번째 전"
}
