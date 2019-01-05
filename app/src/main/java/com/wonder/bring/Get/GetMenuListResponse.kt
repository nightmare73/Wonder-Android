package com.wonder.bring.Get

import com.wonder.bring.data.MenuListData

data class GetMenuListResponse (
    val status : Int,
    val message : String,
    val data :  MenuData
)

data class MenuData(
    val sotreIdx : Int,
    val name : String,
    val address : String,
    val bgPhotoUrl: String,
    val menuList : ArrayList<MenuListData>

)


