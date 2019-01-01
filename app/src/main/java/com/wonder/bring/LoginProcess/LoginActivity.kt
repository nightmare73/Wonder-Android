package com.wonder.bring.LoginProcess

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.widget.Toast
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

    var input_id : String = ""
    var input_pw : String = ""


    // 통신을 위한 변수
    val networkService : NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setOnBtnClickListner()

//        // 자동로그인 로직
//        if(SharedPreferenceController.getAuthorization(this).isNotEmpty()){
//            startActivity<MainActivity>()
//            finish()
//        }


        // edittext입력 시, 로그인버튼 색깔 변하게 하기
        et_login_act_pw.addTextChangedListener(object : TextWatcher{

            override fun afterTextChanged(p0: Editable?) {
                input_id = et_login_act_id.text.toString()
                input_pw = et_login_act_pw.text.toString()

                if(input_id.length > 0 && input_pw.length > 0)
                    btn_login_act_login.isSelected=true
                else
                    btn_login_act_login.isSelected=false

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })


    }


    private fun setOnBtnClickListner(){

        // 로그인
        btn_login_act_login.setOnClickListener {

            if(input_id.length > 0 && input_pw.length > 0)
                getLoginResponse()
            else
                toast("정보를 모두 입력해주세요")

        }

        // 회원가입으로 이동
        tv_login_act_movetosignup.setOnClickListener {
            startActivity<SignupActivity>()
        }

        // 뒤로가기
        btn_login_act_back.setOnClickListener {
            finish()
        }


    }

    // 로그인 통신 로직
    private fun getLoginResponse(){
        if(input_id.isNotEmpty()&&input_pw.isNotEmpty()){

            //json형식의 객체 만들기
            var jsonObject= JSONObject()
            jsonObject.put("id",input_id)
            jsonObject.put("password",input_pw)

            //Gson라이브러리의 json parser를 통해 json으로!
            val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

            Log.d("GsonObject",gsonObject.toString())

            //통신 시작
            val postLoginResponse : Call<PostLogInResponse> = networkService.postLoginResponse("application/json",gsonObject)
            postLoginResponse.enqueue(object : Callback<PostLogInResponse> {
                override fun onResponse(call: Call<PostLogInResponse>, response: Response<PostLogInResponse>) {
                    var body = response!!.body()

                    if(response!!.isSuccessful){  // 1. 로그인 성공

                        if(body!!.message.equals("로그인 성공")) {
                            val token = body!!.data.token

                            // 그 token값을 SharedPreference에 저장.
                            SharedPreferenceController.setAuthorization(this@LoginActivity, token)
                            // 로그인 했으니까 그 전화면으로 이동
                            //todo: finisih만 하면 되는건가? 전화면으로 돌아가야 하니까
                            startActivity<MainActivity>()
                            finish()
                        }else{// 2. 로그인 실패( 아이디 또는 비밀번호 디비랑 맞지 않을때)
                            toast("아이디 또는 비밀번호가 잘못되었습니다.").setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                        }
                    }

                }

                override fun onFailure(call: Call<PostLogInResponse>, t: Throwable) { // 통신 실패
                    Log.d("서버연결 실패",t.toString())

                }
            })
        }
    }
}
