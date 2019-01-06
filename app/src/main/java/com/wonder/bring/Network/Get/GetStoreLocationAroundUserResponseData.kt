package com.wonder.bring.Network.Get

import com.wonder.bring.Network.Get.OtherDataClasses.StoreLocation

data class GetStoreLocationAroundUserResponseData(
    var status: Int,
    var message: String,
    var data : ArrayList<StoreLocation>
)

