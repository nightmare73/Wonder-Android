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
import com.wonder.bring.R.id.btn_signup_act_check_duplicate_off
import com.wonder.bring.R.id.et_signup_act_id
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    var input_id: String = ""

    var idCheckOk = false
    var passEqualOk = false

    lateinit var idTextListener: TextWatcher

    lateinit var passTextListener: TextWatcher

    // 통신을 위한 변수
    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        variableInit()
        viewInit()
        setOnBtnClickListner()

    }

    private fun viewInit() {

        btn_signup_act_next.isEnabled = false

        et_signup_act_id.addTextChangedListener(idTextListener)
        et_signup_act_pw1.addTextChangedListener(passTextListener)
        et_signup_act_pw2.addTextChangedListener(passTextListener)
    }

    private fun variableInit() {

        idTextListener = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

                input_id = et_signup_act_id.text.toString()

                completeCheck()
            }
        }

        passTextListener = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

                if (et_signup_act_pw1.text.toString().equals(et_signup_act_pw2.text.toString())) {

                    tv_signup_act_canusepw.visibility = View.VISIBLE
                    tv_signup_act_canusepw.textColor = Color.BLUE
                    tv_signup_act_canusepw.text = "비밀번호가 일치합니다."
                    passEqualOk = true
                } else {

                    tv_signup_act_canusepw.visibility = View.VISIBLE
                    tv_signup_act_canusepw.textColor = Color.RED
                    tv_signup_act_canusepw.text = "비밀번호가 서로 일치하지 않습니다"
                    passEqualOk = false
                }
                completeCheck()

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        }
    }

    private fun completeCheck() {
        if (idCheckOk && passEqualOk) {

            btn_signup_act_next.isEnabled = true
            btn_signup_act_next.backgroundResource = R.color.salmon
            btn_signup_act_next.textColor = Color.parseColor("#FFFFFF")
        } else {

            btn_signup_act_next.isEnabled = false
            btn_signup_act_next.backgroundResource = R.color.white
            btn_signup_act_next.textColor = Color.parseColor("#909090")
        }
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
                                idCheckOk = true

                                completeCheck()
                            }
                            if (response.body()!!.status == 400) {
                                tv_signup_act_canuseid.visibility = View.VISIBLE
                                tv_signup_act_canuseid.text = "사용 할 수 없는 아이디 입니다"
                                tv_signup_act_canuseid.textColor = Color.RED

                                Log.v("malibin debug", "중복체크 실패 : " + response.body().toString())
                                idCheckOk = false
                            }
                        }

                        override fun onFailure(call: Call<GetCheckDuplicateIdResponseData>, t: Throwable) {
                            toast("서버통신에 실패하였습니다.")
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
            toast("버튼 눌림")
        }
    }
}

