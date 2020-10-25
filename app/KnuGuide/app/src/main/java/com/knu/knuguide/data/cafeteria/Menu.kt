package com.knu.knuguide.data.cafeteria

import com.knu.knuguide.data.KNUData

data class Menu (
    var menus: String = "") : KNUData {

    var menuType = MenuType.BREAKFAST

    override fun getRecyclerType(): Int = KNUData.Type.ITEM_CAFETERIA_MENU
}

enum class MenuType {
    BREAKFAST, LUNCH, DINNER
}