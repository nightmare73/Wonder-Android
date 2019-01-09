package com.wonder.bring

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.wonder.bring.LoginProcess.LoginActivity
import com.wonder.bring.LoginProcess.SignupActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_mypage.*
import org.jetbrains.anko.startActivity

class MypageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)
        setOnBtnClickListner()


    }

    private fun setOnBtnClickListner() {

        // 메인페이지로 이동
        btn_mypage_act_back.setOnClickListener {
            startActivity<MainActivity>()
        }

        // 로그인으로 이동
        btn_mypage_act_logout.setOnClickListener {
            startActivity<LoginActivity>()
        }
    }
}
