package com.wonder.bring.db

import android.content.Context
import com.wonder.bring.MainFragment.MyFragmentStatePagerAdapter


object SharedPreferenceController {
    private val USER_NAME = "MYKEY"
    private val myAuth = "myAuth"


    fun setAuthorization(context: Context, authorization: String) {
        val pref = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.putString(myAuth, authorization)
        editor.commit()
    }


    fun getAuthorization(context: Context): String {
        val pref = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        return pref.getString(myAuth, "")
    }


    fun clearSPC(context: Context) {
        val pref = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.clear()
        editor.commit()
    }

    fun setId(context: Context, userId: String) {
        val pref = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("userId", userId)
        editor.commit()
    }

    fun getId(context: Context): String {
        val pref = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE)
        return pref.getString("userId", "")
    }

    fun setCartData(context: Context, userId: String, cartData: String) {
        val pref = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(userId + "Cart", cartData)
        editor.commit()
    }

    fun getCartData(context: Context, userId: String): String {
        val pref = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE)
        return pref.getString(userId + "Cart", "")
    }

    fun deleteCartData(context: Context, userId: String){

    }
}