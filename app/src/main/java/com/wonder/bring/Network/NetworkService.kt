package com.wonder.bring.Network

import com.google.gson.JsonObject
import com.wonder.bring.Network.Get.GetStoreLocationAroundUserResponseData
import com.wonder.bring.Post.PostLogInResponse
import retrofit2.Call
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface NetworkService {

    // 로그인
    @POST("/login")
    fun postLoginResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Call<PostLogInResponse>

    @GET("/maps")
    fun getStoreLocationAroundUserRequest(
        @Header("Content-Type") content_type: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String
    ): Call<GetStoreLocationAroundUserResponseData>

}