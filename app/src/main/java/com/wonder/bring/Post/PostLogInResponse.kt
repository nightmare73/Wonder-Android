package com.wonder.bring.Post

data class PostLogInResponse(
    val status : Int,
    val message : String,
    val data : LoginData
)

data class LoginData(
    val token : String
)