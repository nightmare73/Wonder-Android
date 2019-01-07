package com.wonder.bring.Network.Get

import com.wonder.bring.Network.Get.OtherDataClasses.MenuDetails

data class GetMenuDetailsResponseData(
    var status: Int,
    var message: String,
    var data : MenuDetails
)