package com.wonder.bring.Network.Get.OtherDataClasses

data class OrderDetail(
    var name: String,
    var size: Int,
    var orderCount: Int,
    var totalPrice: Int,
    var memo: String
)