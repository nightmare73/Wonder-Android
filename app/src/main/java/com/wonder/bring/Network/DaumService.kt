package com.wonder.bring.Network

import com.wonder.bring.Network.Get.GetDaumAdressResponseData
import com.wonder.bring.Network.Get.GetDaumKeywordAddressResponseData
import retrofit2.Call
import retrofit2.http.*

interface DaumService {


    @GET("/v2/local/search/address.json")
    fun getDaumAdressRequest(
        @Header("Authorization") authorization: String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int
        ): Call<GetDaumAdressResponseData>


    @GET("/v2/local/search/keyword.json")
    fun getDaumKeywordAdressRequest(
        @Header("Authorization") authorization: String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<GetDaumKeywordAddressResponseData>

}