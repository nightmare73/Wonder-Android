package com.wonder.bring.Network

import com.google.gson.JsonObject
import com.wonder.bring.Network.Get.GetMenuListResponse
import com.wonder.bring.Network.Get.GetStoreInfoResponse
import com.wonder.bring.Network.Get.GetCheckDuplicateIdResponseData
import com.wonder.bring.Network.Get.GetCheckDuplicateNickResponseData
import com.wonder.bring.Network.Post.PostLogInResponse
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {

    // 로그인
    @POST("/login")
    fun postLoginResponse(
        @Header("Content-Type") content_type:String,
        @Body() body: JsonObject
    ) : Call<PostLogInResponse>

    @GET("/users/check")
    fun getCheckDuplicateIdRequest(
        @Header("Content-Type") content_type: String,
        @Query ("id") id:String

    ) : Call<GetCheckDuplicateIdResponseData>

    @GET("/users/check")
    fun getCheckDuplicateNickRequest(
        @Header("Content-Type") content_type: String,
        @Query ("nick") id:String

    ) : Call<GetCheckDuplicateNickResponseData>


    // 가게Activity / 매장 메뉴 리스트
    @GET("/stores/{storeIdx}/menu")
    fun getMenuListResponse(
        @Header("Content-Type") content_type : String,
        @Path("storeIdx") store_idx : Int
    ) : Call<GetMenuListResponse>

    // 가게Activity / 매장 상세정보
    @GET("/stores/{storeIdx}")
    fun getStoreInfoResponse(
        @Header("Content-Type") content_type : String,
        @Path("storeIdx") store_idx : Int
    ) : Call<GetStoreInfoResponse>


}