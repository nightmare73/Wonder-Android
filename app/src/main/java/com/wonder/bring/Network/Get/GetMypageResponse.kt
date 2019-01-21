package com.wonder.bring.Network.Get

import com.wonder.bring.Network.Get.OtherDataClasses.MypageData

data class GetMypageResponse (
    val status : Int,
    val message : String,
    val data : MypageData
)



