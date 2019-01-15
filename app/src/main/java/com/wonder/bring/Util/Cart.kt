package com.wonder.bring.Util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.wonder.bring.db.CartData
import com.wonder.bring.db.SharedPreferenceController
import java.lang.reflect.Type

class Cart(var ctx: Context) {

    private val gson: Gson = GsonBuilder().create()

    fun loadCartDataString(userId: String): String = SharedPreferenceController.getCartData(ctx, userId)

    fun loadCartData(userId: String): ArrayList<CartData> {

        val result: ArrayList<CartData>
        val type: Type = object : TypeToken<ArrayList<CartData>>() {}.type

        val loadedCartData = loadCartDataString(userId)

        result = gson.fromJson(loadedCartData, type)

        return result
    }

    fun saveCartData(userId: String, data: ArrayList<CartData>) =
        SharedPreferenceController.setCartData(ctx, userId, gson.toJson(data))

    fun addCartList(userId: String, data: CartData) {

        var currentCartData: ArrayList<CartData> = loadCartData(userId)
        currentCartData.add(data)
        saveCartData(userId, currentCartData)
    }

    fun deleteCartData(userId: String, position: Int) {

        var currentCartData: ArrayList<CartData> = loadCartData(userId)
        currentCartData.removeAt(position)
        saveCartData(userId, currentCartData)
    }

    fun modifyCartData(userId: String, position: Int, quantity: Int){

        var currentCartData: ArrayList<CartData> = loadCartData(userId)
        currentCartData[position].quantity = quantity
        saveCartData(userId, currentCartData)
    }

}