package com.knu.knuguide.data.bus

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(strict = false, name = "response")
data class RouteBus (
    @field:Element(name = "header")
    var header: Header? = null,
    @field:Element(name = "body")
    var body: RouteBusBody? = null
)

@Root(strict = false, name = "body")
data class RouteBusBody(
    @field:ElementList(name = "items")
    var items: MutableList<RouteBusItem> = mutableListOf(),
    @field:Element(name = "totalCount")
    var totalCount: Int = 0
)

@Root(strict = false, name = "item")
data class RouteBusItem(
    @field:Element(name = "gpslati")
    var gpsLatitude: Double = 0.0,
    @field:Element(name = "gpslong")
    var gpsLongitude: Double = 0.0,
    @field:Element(name = "nodeid")
    var nodeId: String = "",
    @field:Element(name = "nodenm")
    var nodeName: String = "",
    @field:Element(name = "nodeord")
    var nodeOrder: Int = 0,
    @field:Element(name = "vehicleno")
    var viechleNo: String = ""
)