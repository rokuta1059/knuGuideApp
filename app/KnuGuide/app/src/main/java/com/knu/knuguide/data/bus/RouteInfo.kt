package com.knu.knuguide.data.bus

import com.knu.knuguide.data.KNUData
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(strict = false, name = "response")
data class RouteInfo (
    @field:Element(name = "header")
    var header: Header? = null,
    @field:Element(name = "body")
    var body: RouteInfoBody? = null
)

@Root(strict = false, name = "body")
data class RouteInfoBody(
    @field:ElementList(name = "items")
    var items: MutableList<RouteInfoItem> = mutableListOf()
)

@Root(strict = false, name = "item")
data class RouteInfoItem(
    @field:Element(name = "endnodenm")
    var endNodeName: String = "",
    @field:Element(name = "startnodenm")
    var startNodeName: String = ""
) : KNUData {
    override fun getRecyclerType(): Int = KNUData.Type.ITEM_BUS_ROUTE_INTERVAL
}

