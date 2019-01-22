package com.wonder.bring.Network.Get

import com.wonder.bring.Network.Get.OtherDataClasses.MenuListData

data class GetMenuListResponse (
    val status : Int,
    val message : String,
    val data : MenuData
)

data class MenuData(
    val sotreIdx : Int,
    val bgPhotoUrl: String,
    val menuList : ArrayList<MenuListData>

)


