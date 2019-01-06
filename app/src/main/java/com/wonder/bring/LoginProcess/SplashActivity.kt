package com.wonder.bring.LoginProcess

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.wonder.bring.MainActivity
import com.wonder.bring.R
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().apply {
            postDelayed({
                //2초뒤에 실행될 코드를 여기에 작성

                //ANKO 라이브러리
                startActivity<MainActivity>("data1" to "Hello", "data2" to "World!", "data3" to 1000)
//                val intent : Intent= Intent(this@SplashActivity,MainActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                finish()
                //finish를 안하면 밑에 깔려잇음. 제거해줌으로 쌓임 방지

                //일반 코틀린 문법
//                val intent : Intent = Intent(this@SplashActivity,MainActivity::class.java) //보내는 액티비티, 목적지 액티비티 가 인텐트 안의 매개변수래
//                intent.putExtra("data1", "Hello")
//                intent.putExtra("data2","World!")
//                intent.putExtra("data3",1000)
//                startActivity(intent);

            }, 2000)//인위적으로 딜레이를 주는 함수
            //{} 이게 뭐냐면 람다식을 쓸수 잇는거래


        }
    }
}