package com.wonder.bring.Network.Get

import com.wonder.bring.Network.Get.OtherDataClasses.OrderListData

data class GetOrderListResponse (
    val status : Int,
    val message : String,
    val data : Orderdata
)

data class Orderdata(
    val nick : String,
    val orderList : ArrayList<OrderListData>

)


