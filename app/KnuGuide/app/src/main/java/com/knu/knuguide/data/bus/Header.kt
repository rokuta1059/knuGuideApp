package com.knu.knuguide.data.bus

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false, name = "header")
data class Header (
    @field:Element(name = "resultCode")
    var resultCode: String = "",
    @field:Element(name = "resultMsg")
    var resultMsg: String = ""
)