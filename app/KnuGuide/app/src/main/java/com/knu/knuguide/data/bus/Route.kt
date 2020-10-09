package com.knu.knuguide.data.bus

import com.knu.knuguide.R
import com.knu.knuguide.data.KNUData
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(strict = false, name = "response")
data class Route (
    @field:Element(name = "header")
    var header: Header? = null,
    @field:Element(name = "body")
    var body: RouteBody? = null
)

@Root(strict = false, name = "body")
data class RouteBody(
    @field:ElementList(name = "items")
    var items: MutableList<RouteItem> = mutableListOf()
)

@Root(strict = false, name = "item")
data class RouteItem(
    @field:Element(name = "gpslati")
    var gpsLatitude: Double = 0.0,
    @field:Element(name = "gpslong")
    var gpsLongitude: Double = 0.0,
    @field:Element(name = "nodeid")
    var nodeId: String = "",
    @field:Element(name = "nodenm")
    var nodeName: String = "",
    @field:Element(name = "nodeno")
    var nodeNumber: Int = 0,
    @field:Element(name = "nodeord")
    var nodeOrder: Int = 0,
    @field:Element(name = "routeid")
    var routeId: String = "",
    @field:Element(name = "updowncd")
    var upDownCd: Int = 0,

    var hasSticker: Boolean = false,
    var stkGpsLatitude: Double = 0.0,
    var stkGpsLongitude: Double = 0.0,
    var vehicleNo: String = ""
) : KNUData {
    override fun getRecyclerType(): Int = KNUData.Type.ITEM_BUS_ROUTE

    fun getViechleNumberLastDigit(): String {
        return if (vehicleNo.length - 4 >= 0) {
            vehicleNo.substring(vehicleNo.length - 4, vehicleNo.length)
        } else ""
    }
}

data class RouteBusCount(
    var totalCount: Int) : KNUData {

    override fun getRecyclerType(): Int = KNUData.Type.ITEM_BUS_ROUTE_HEADER
}
