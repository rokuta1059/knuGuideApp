package com.knu.knuguide.data.bus

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(strict = false, name = "response")
data class RouteInfo (
    @field:Element(name = "header")
    var header: Header? = null,
    @field:Element(name = "body")
    var body: Body? = null
)

@Root(strict = false, name = "header")
data class Header (
    @field:Element(name = "resultCode")
    var resultCode: String = "",
    @field:Element(name = "resultMsg")
    var resultMsg: String = ""
)

@Root(strict = false, name = "body")
data class Body(
    @field:ElementList(name = "items")
    var items: MutableList<Item> = mutableListOf()
)

@Root(strict = false, name = "item")
data class Item(
    @field:Element(name = "endnodenm")
    var endNodeName: String = "",
    @field:Element(name = "startnodenm")
    var startNodeName: String = ""
)
