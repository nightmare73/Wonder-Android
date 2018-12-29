package com.wonder.bring.LoginProcess

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.wonder.bring.MainActivity
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.NetworkService
import com.wonder.bring.Post.PostLogInResponse
import com.wonder.bring.R
import com.wonder.bring.db.SharedPreferenceController
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    // 통신을 위한 변수
    val networkService : NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setOnBtnClickListner()

        // 자동로그인 로직
        if(SharedPreferenceController.getAuthorization(this).isNotEmpty()){
            startActivity<MainActivity>()
            finish()
        }

    }

    private fun setOnBtnClickListner(){
        btn_join_join.setOnClickListener {
            getLoginResponse()
        }
    }

    // 로그인 통신 로직
    private fun getLoginResponse(){
        if(et_id_join.text.toString().isNotEmpty()&&et_pw_join1.text.toString().isNotEmpty()){
            //edittext있는 값 받기
            val input_email : String=et_id_join.text.toString()
            val input_pw : String= et_pw_join1.text.toString()

            //json형식의 객체 만들기
            var jsonObject= JSONObject()
            jsonObject.put("id",input_email)
            jsonObject.put("password",input_pw)


            //Gson라이브러리의 json parser를 통해 json으로!
            val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

            Log.d("gsonObject",gsonObject.toString())

            //통신 시작
            val postLoginResponse : Call<PostLogInResponse> = networkService.postLoginResponse("application/json",gsonObject)
            postLoginResponse.enqueue(object : Callback<PostLogInResponse> {
                override fun onResponse(call: Call<PostLogInResponse>, response: Response<PostLogInResponse>) {

                    val token =response.body()!!.data.token

                    // 그 token값을 SharedPreference에 저장.
                    SharedPreferenceController.setAuthorization(this@LoginActivity,token)
                    // 로그인 했으니까 home으로 이동
                    startActivity<MainActivity>()
                    finish()
                }

                override fun onFailure(call: Call<PostLogInResponse>, t: Throwable) { // 통신 실패
                    Log.d("Sign Up Fail",t.toString())

                }
            })
        }
    }
}
