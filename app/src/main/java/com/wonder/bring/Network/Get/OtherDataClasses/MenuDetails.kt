package com.wonder.bring.Network.Get.OtherDataClasses

data class MenuDetails(
    var storeIdx: Int,
    var menuIdx: Int,
    var name: String,
    var photoUrl: String,
    var sizePrices: ArrayList<MenuSize>
)