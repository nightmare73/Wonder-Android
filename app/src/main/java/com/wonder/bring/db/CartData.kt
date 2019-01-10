package com.wonder.bring.db

data class CartData(
    val storeIdx: Int,
    val menuIdx: Int,

    val imageUrl: String,
    val storeName: String,
    val menuName: String,
    val request: String,

    var quantity: Int,
    val size: Int,
    var cost: Int
)