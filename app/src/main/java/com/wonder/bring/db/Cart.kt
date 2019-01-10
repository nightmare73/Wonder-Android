package com.wonder.bring.db

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class Cart {

    //get set 다 사용가능
    constructor(
        ctx: Context,
        userId: String,
        cartdata: ArrayList<CartData>
    ) {
        this.ctx = ctx
        cartUserId = userId
        this.cartdata = cartdata
    }

    //이 경우에는 get만 사용가능 막는방법을 모르겠다.
    //set도 쓸수는 잇는데 쓰면 장바구니에 nothing이 들어가버리는 참사가 발생함
    constructor(ctx: Context, userId: String) {
        this.ctx = ctx
        cartUserId = userId
    }

    //key 값은 유저 아이디값 으로 한다.
    private var ctx: Context
    private var cartUserId: String
    private lateinit var cartdata: ArrayList<CartData>

    private var gson: Gson = GsonBuilder().create()
    var stringCartData: String = gson.toJson(cartdata)

    var db = SharedPreferenceController

    //카트데이터없이 그냥 써버리면 대참사
    fun saveCart() {
        db.setCartData(ctx, cartUserId, stringCartData)
        Log.v("Malibin Cart Debug", "이 객체의 Json : $stringCartData")
    }

    fun getCart(): String {
        return db.getCartData(ctx, cartUserId)
    }
}
