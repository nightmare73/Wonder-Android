package com.wonder.bring.Network.Post

import com.wonder.bring.Network.Get.OtherDataClasses.OrderList

data class PostOrderRequest(

    var storeIdx: Int,
    var orderMenuList: ArrayList<OrderList>

)