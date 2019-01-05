package com.wonder.bring.Get

import com.wonder.bring.data.MenuListData

data class GetStoreInfoResponse (
    val status : Int,
    val message : String,
    val data : StoreInfoData
)

data class StoreInfoData(
    val time: String,
    val breakDays : String,
    val number : String
)


