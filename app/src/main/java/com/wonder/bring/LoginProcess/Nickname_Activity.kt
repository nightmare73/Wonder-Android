package com.wonder.bring.LoginProcess

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.wonder.bring.R

class Nickname_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nickname_)
        setOnBtnClickListner()
    }

    private fun setOnBtnClickListner(){


        // 닉네임중복체크
//        btn_nickname_act_check_duplicate_off.setOnClickListener {
//
//            if(input_id.length > 0 && input_id.length > 0)
//                getLoginResponse()
//            else
//                toast("사용불가능한 닉네임입니다.")


        // 다음으로 이동  닉네임설정
//        btn_nickname_act_next.setOnClickListener {
//            startActivity<>()
//        }


    }
}
