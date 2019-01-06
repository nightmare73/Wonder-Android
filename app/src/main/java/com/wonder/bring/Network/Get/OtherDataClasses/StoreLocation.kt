package com.wonder.bring.Network.Get.OtherDataClasses

data class StoreLocation(
    var storeIdx : Int,
    var storeName: String,
    var latitude : Double,
    var longitude : Double,
    var distance: String
)