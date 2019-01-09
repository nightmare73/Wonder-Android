package com.wonder.bring.Network.Get

import com.wonder.bring.Network.Get.OtherDataClasses.OrderDetailList

data class OrderDetailListResponseData(
    var status: Int,
    var message: String,
    var data: OrderDetailList
)