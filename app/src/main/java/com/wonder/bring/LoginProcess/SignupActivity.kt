package com.wonder.bring.LoginProcess

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.wonder.bring.R
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        setOnBtnClickListner()


    }




    private fun setOnBtnClickListner(){

//        // id중복체크
//        btn_signup_act_check_duplicate_off.setOnClickListener {
//
//            if(input_id.length > 0 && input_id.length > 0)
//                getLoginResponse()
//            else
//                toast("사용불가능한 아이디입니다.")

        }

//        // 다음으로 이동  닉네임설정
//        btn_signup_act_next.setOnClickListener {
//            startActivity<Nickname_Activity>()
//        }

        // 뒤로가기
//        btn_signup_act_back.setOnClickListener {
//            finish()
//        }

//다음으로 넘어가는 함수 if 위에 결과값들이 다 정확하면 startactivity(nicknameactiviy)() else 정보를 정확히 입력해주세요  토스트?
// btn_login_act_login.setOnClickListener {
//
//            if(input_id.length > 0 && input_pw.length > 0)
//                getLoginResponse()
//            else
//                toast("정보를 모두 입력해주세요")
// 아이디 중복체크해서 밑에 뜨는 통신들 1.아이디만 틀렸을경우 2. 비밀번호만 안맞았을경우 3.둘다 틀렸을경우 3가지
    }

