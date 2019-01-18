package com.wonder.bring.LoginProcess

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.NetworkService
import com.wonder.bring.Network.Post.PostSignupResponseData
import com.wonder.bring.R
import kotlinx.android.synthetic.main.activity_agreement.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textColor
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgreementActivity : AppCompatActivity() {

    var id: String = ""
    var pass: String = ""
    var nick: String = ""

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement)

        variableInit()
        setCheckBoxListener()

        submitButtonInit()

        Log.v("Malibin Debug","넘어온 변수들 id : $id, pass : $pass, nick : $nick")
    }

    private fun variableInit(){

        id = intent.getStringExtra("id")
        pass = intent.getStringExtra("pass")
        nick = intent.getStringExtra("nick")
    }

    private fun setCheckBoxListener() {

        cb_agree_act_whole.setOnClickListener {

            if (cb_agree_act_whole.isChecked) {
                cb_agree_act_one.isChecked = true
                cb_agree_act_two.isChecked = true
            } else {
                cb_agree_act_one.isChecked = false
                cb_agree_act_two.isChecked = false
            }
            completeCheck()
        }

        cb_agree_act_one.setOnClickListener {

            checkWhole()
            completeCheck()
        }

        cb_agree_act_two.setOnClickListener {

            checkWhole()
            completeCheck()
        }
    }

    private fun checkWhole() {
        if (cb_agree_act_one.isChecked && cb_agree_act_two.isChecked)
            cb_agree_act_whole.isChecked = true
        else
            cb_agree_act_whole.isChecked = false
    }

    private fun completeCheck() {
        if (cb_agree_act_whole.isChecked) {
            btn_agree_act_submit.isEnabled = true
            btn_agree_act_submit.backgroundResource = R.color.salmon
            btn_agree_act_submit.textColor = Color.parseColor("#FFFFFF")
        } else {
            btn_agree_act_submit.isEnabled = false
            btn_agree_act_submit.backgroundResource = R.color.white
            btn_agree_act_submit.textColor = Color.parseColor("#909090")
        }
    }

    private fun submitButtonInit(){

        btn_agree_act_submit.setOnClickListener {

            requestSignup()
        }
    }

    private fun requestSignup(){

        var _id = RequestBody.create(MediaType.parse("text/plain"), id)
        var _pass = RequestBody.create(MediaType.parse("text/plain"), pass)
        var _nick = RequestBody.create(MediaType.parse("text/plain"), nick)

        networkService.postSignupRequest("multipart/form-data",_id,_pass,_nick,null).enqueue(object : Callback<PostSignupResponseData>{
            override fun onFailure(call: Call<PostSignupResponseData>, t: Throwable) {
                toast("서버통신에 실패하였습니다.")
            }

            override fun onResponse(call: Call<PostSignupResponseData>, response: Response<PostSignupResponseData>) {

                Log.v("Malibin Debug",response.body().toString())

                if(response.isSuccessful){


                    var branch = response.body()!!.status

                    when(branch){
                        201->{


                            var intent = Intent(applicationContext, LoginActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            toast("회원 가입이 완료되었습니다!")

                        }
                        else ->{
                            toast("회원 가입에 실패하였습니다.")
                        }
                    }
                }
            }

        })

    }


}