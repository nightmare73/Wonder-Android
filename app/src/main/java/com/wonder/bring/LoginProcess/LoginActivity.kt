package com.wonder.bring.LoginProcess

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.wonder.bring.Firebase.FireBaseMessagingService
import com.wonder.bring.MainActivity
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.NetworkService
import com.wonder.bring.Network.Post.PostLogInResponse
import com.wonder.bring.R
import com.wonder.bring.R.id.btn_login_act_login
import com.wonder.bring.R.id.et_login_act_pw
import com.wonder.bring.db.SharedPreferenceController
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    var input_id: String = ""
    var input_pw: String = ""

    lateinit var ctx: Context

    // backpressed변수
    var backPressedTime: Long = 0

    // 통신을 위한 변수
    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewInit()
    }

    private fun viewInit() {

        btn_login_act_login.isEnabled = false

        // 회원가입으로 이동
        tv_login_act_movetosignup.setOnClickListener {
            startActivity<SignupActivity>()
        }

        // 뒤로가기
        btn_login_act_back.setOnClickListener {
            finish()
        }

        et_login_act_id.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    checkEmpty()
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

            })

        et_login_act_pw.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    checkEmpty()
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            })

        btn_login_act_login.setOnClickListener {
            getLoginResponse()
            Log.v("malibin debug","로그인버튼눌림")
        }

    }

    fun checkEmpty() {
        if (et_login_act_id.text.isNotEmpty() && et_login_act_pw.text.isNotEmpty()) {
            btn_login_act_login.backgroundResource = R.color.salmon
            tv_logintext.textColor = Color.parseColor("#FFFFFF")
            btn_login_act_login.isEnabled = true
        } else {
            btn_login_act_login.backgroundResource = R.color.white
            tv_logintext.textColor = Color.parseColor("#909090")
            btn_login_act_login.isEnabled = false
        }

        input_id = et_login_act_id.text.toString()
        input_pw = et_login_act_pw.text.toString()
    }

    // 로그인 통신 로직
    private fun getLoginResponse() {
        if (input_id.isNotEmpty() && input_pw.isNotEmpty()) {


            //json형식의 객체 만들기
            var jsonObject = JSONObject()
            jsonObject.put("id", input_id)
            jsonObject.put("password", input_pw)

            //Gson라이브러리의 json parser를 통해 json으로!
            val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

            Log.d("GsonObject", gsonObject.toString())

            //통신 시작
            val postLoginResponse: Call<PostLogInResponse> =
                networkService.postLoginResponse("application/json", gsonObject)
            postLoginResponse.enqueue(object : Callback<PostLogInResponse> {
                override fun onResponse(call: Call<PostLogInResponse>, response: Response<PostLogInResponse>) {
                    var body = response!!.body()

                    Log.v("Malibin Debug","응답 : " + response.body().toString())
                    if (response!!.isSuccessful) {  // 1. 로그인 성공

                        if (body!!.message.equals("로그인 성공")) {
                            val token = body!!.data.token

                            // 그 token값을 SharedPreference에 저장.
                            SharedPreferenceController.setAuthorization(this@LoginActivity, token)

                            // todo : 이부분 어찌 인텐트 전환해줘야 할지 다시 한번 생각할 필요 있음.
                            // todo: main -> Login -> 다시 main으로 가는데 아예 새롭게 main을 호출해줘야해
                            // 로그인 했으니까 그 전화면으로 이동
                            //val intent: Intent = Intent(this@LoginActivity, MainActivity::class.java)
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                            var intent = Intent()
                            intent.putExtra("isLogin",true)
                            setResult(Activity.RESULT_OK, intent)

                            Log.v("Malibin Debug","로그인 성공후 finish 직전")

                            finish()

                        } else {// 2. 로그인 실패( 아이디 또는 비밀번호 디비랑 맞지 않을때)
                            toast("아이디 또는 비밀번호가 잘못되었습니다.").setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                        }
                    }

                }

                override fun onFailure(call: Call<PostLogInResponse>, t: Throwable) { // 통신 실패
                    Log.d("서버연결 실패", t.toString())

                }
            })
        }
    }

//    override fun onBackPressed() {
//            super.onBackPressed()
//        overridePendingTransition(R.anim.slide_in_down,0)
//    }


    fun callMainAct(){
        if(ctx is MainActivity)
            (ctx as MainActivity).settingView(true)
    }
}
