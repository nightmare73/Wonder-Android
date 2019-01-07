package com.wonder.bring.Network.Get.OtherDataClasses

data class StoreSummary(
    var name: String,
    var type: String,
    var address: String,
    var number: String,
    var photoUrl: ArrayList<String>
)