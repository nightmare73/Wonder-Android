package com.wonder.bring.Network.Get

import com.wonder.bring.Network.Get.OtherDataClasses.MenuDetails
import com.wonder.bring.Network.Get.OtherDataClasses.MenuSize

data class GetMenuDetailsResponseData(

    var status: Int,
    var message: String,
    var data : ArrayList<MenuSize>

)