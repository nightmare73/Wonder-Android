package com.wonder.bring.LoginProcess

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.Get.GetCheckDuplicateIdResponseData
import com.wonder.bring.Network.NetworkService
import com.wonder.bring.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textColor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    var input_id: String = ""
    var input_pw1: String = ""
    var input_pw2: String = ""

    // 통신을 위한 변수
    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        setOnBtnClickListner()

        // edittext 다 채워지면 , 완료버튼 색깔 변하게 하기  근데 123 중 한개라도 빠져있으면 다음 버튼 활성화 안되게 하기
        et_signup_act_pw2.addTextChangedListener(object : TextWatcher{

            override fun afterTextChanged(p0: Editable?) {
                input_id = et_signup_act_id.text.toString()
                input_pw1 = et_signup_act_pw1.text.toString()
                input_pw2 = et_signup_act_pw2.text.toString()

                if(input_id.length > 0 && input_pw1.length > 0 && input_pw2.length > 0)
                    btn_signup_act_next.isSelected=true
                else
                    btn_signup_act_next.isSelected=false

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

    }

    private fun setOnBtnClickListner() {

        // id중복체크    띄어쓰기 안막음
        btn_signup_act_check_duplicate_off.setOnClickListener {

            input_id = et_signup_act_id.text.toString()

            if (!input_id.equals("")) {

                networkService.getCheckDuplicateIdRequest("application/json", input_id)
                    .enqueue(object : Callback<GetCheckDuplicateIdResponseData> {
                        override fun onResponse(
                            call: Call<GetCheckDuplicateIdResponseData>,
                            response: Response<GetCheckDuplicateIdResponseData>
                        ) {
                            Log.v("malibin debug", "서버통신성공 : " + response.body().toString())
                            if (response.body()!!.status == 200) {
//                               이건 서버통신 성공해서 중복체크가 성공적으로 완료될때
                                tv_signup_act_canuseid.visibility = View.VISIBLE
                                tv_signup_act_canuseid.text = "사용 할 수 있는 아이디 입니다"
                                tv_signup_act_canuseid.textColor = Color.BLUE
                                Log.v("malibin debug", "중복체크 성공 : " + response.body().toString())
                            }
                            if (response.body()!!.status == 400) {
                                tv_signup_act_canuseid.visibility = View.VISIBLE
                                tv_signup_act_canuseid.text = "사용 할 수 없는 아이디 입니다"
                                tv_signup_act_canuseid.textColor = Color.RED

                                Log.v("malibin debug", "중복체크 실패 : " + response.body().toString())
                            }
                        }

                        override fun onFailure(call: Call<GetCheckDuplicateIdResponseData>, t: Throwable) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    })

            }
        }

//             뒤로가기
        btn_signup_act_back.setOnClickListener {
            finish()
        }

//다음으로 넘어가는 함수 if 위에 결과값들이 다 정확하면 startactivity(nicknameactiviy)() else 정보를 정확히 입력해주세요
        btn_signup_act_next.setOnClickListener {

//            패스워드가 같으면 다음 닉네임 액티비티로 이동한다!  서로 같지않으면 비밀번호가 일치하지않는다 호출
            if (input_pw1 == input_pw2) {
                startActivity<NicknameActivity>()
            }
            else {
                tv_signup_act_canusepw.visibility = View.VISIBLE
                tv_signup_act_canusepw.text = "비밀번호가 서로 일치하지 않습니다"
                tv_signup_act_canusepw.textColor = Color.RED
            }
        }
    }
}

