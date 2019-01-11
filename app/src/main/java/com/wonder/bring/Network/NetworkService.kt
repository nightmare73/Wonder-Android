package com.wonder.bring.Network

import com.google.gson.JsonObject
import com.wonder.bring.Network.Get.*

import com.wonder.bring.Network.Post.PostLogInResponse
import com.wonder.bring.Network.Post.PostOrderRequest
import com.wonder.bring.Network.Post.PostOrderResponse
import com.wonder.bring.Network.Post.PostSignupResponseData
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.http.*

interface NetworkService {

    // 로그인
    @POST("/login")
    fun postLoginResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Call<PostLogInResponse>

    //회원 가입
    @Multipart
    @POST("/users")
    fun postSignupRequest(
        @Header("Authorization") content_type: String,
        @Part("id") id: RequestBody,
        @Part("password") password: RequestBody,
        @Part("nick") nick: RequestBody,
        @Part profile: MultipartBody.Part?
    ): Call<PostSignupResponseData>


    @GET("/maps")
    fun getStoreLocationAroundUserRequest(
        @Header("Content-Type") content_type: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String
    ): Call<GetStoreLocationAroundUserResponseData>


    @GET("/users/check")
    fun getCheckDuplicateIdRequest(
        @Header("Content-Type") content_type: String,
        @Query("id") id: String

    ): Call<GetCheckDuplicateIdResponseData>

    @GET("/users/check")
    fun getCheckDuplicateNickRequest(
        @Header("Content-Type") content_type: String,
        @Query("nick") id: String

    ): Call<GetCheckDuplicateNickResponseData>


    // 가게Activity / 매장 메뉴 리스트
    @GET("/stores/{storeIdx}/menu")
    fun getMenuListResponse(
        @Header("Content-Type") content_type: String,
        @Path("storeIdx") store_idx: Int
    ): Call<GetMenuListResponse>

    // 가게Activity / 매장 상세정보
    @GET("/stores/{storeIdx}")
    fun getStoreInfoResponse(
        @Header("Content-Type") content_type: String,
        @Path("storeIdx") store_idx: Int
    ): Call<GetStoreInfoResponse>


    // 주문리스트 조회
    @GET("/orders")
    fun getOrderListResponse(
        @Header("authorization") authorization: String
    ): Call<GetOrderListResponse>

    //선택한 매장 조회
    @GET("/maps/stores/{storeIdx}")
    fun getSelectedStoreSummaryRequest(
        @Header("Content-Type") content_type: String,
        @Path("storeIdx") storeIdx: Int
    ): Call<GetSelectedStoreSummaryResponseData>


    //메뉴 상세 정보 조회
    @GET("/stores/{storeIdx}/menu/{menuIdx}")
    fun getMenuDetailsRequest(
        @Header("Content-Type") content_type: String,
        @Path("storeIdx") storeIdx: Int,
        @Path("menuIdx") menuIdx: Int
    ): Call<GetMenuDetailsResponseData>

    //현재 주문 내역 상세 조회
    @GET("/orders/{orderIdx}")
    fun getOrderDetailListRequest(
        @Header("Content-Type") content_type: String,
        @Header("Authorization") authorization: String,
        @Path("orderIdx") orderIdx: Int
    ): Call<GetOrderDetailListResponseData>

    //주문하기
    @POST("/orders")
    fun postOrderResponse(
       @Header("Authorization") authorization: String,
        @Body() body: PostOrderRequest
    ): Call<PostOrderResponse>



    //토큰 유효성 검사
    @GET("/login")
    fun getTokenValidationRequest(
        @Header("Authorization") authorization: String
    ): Call<GetTokenValidationResponseData>

}