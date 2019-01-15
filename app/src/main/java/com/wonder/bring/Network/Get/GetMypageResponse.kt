package com.wonder.bring.Network.Get

import com.wonder.bring.data.MenuListData
import com.wonder.bring.data.MypageData

data class GetMypageResponse (
    val status : Int,
    val message : String,
    val data : MypageData
)



