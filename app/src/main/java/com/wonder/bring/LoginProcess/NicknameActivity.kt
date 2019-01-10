package com.wonder.bring.LoginProcess

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.Get.GetCheckDuplicateNickResponseData
import com.wonder.bring.Network.NetworkService
import com.wonder.bring.R
import kotlinx.android.synthetic.main.activity_nickname.*
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textColor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NicknameActivity : AppCompatActivity() {

    var input_nickname: String = ""
    var id: String = ""
    var pass: String = ""

    var canUseCurrentNick = false

    // 통신을 위한 변수
    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    companion object {
        public final lateinit var NA :AppCompatActivity
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nickname)

        NA = this

        variableInit()
        setOnBtnClickListner()

    }

    private fun variableInit() {
        id = intent.getStringExtra("id")
        pass = intent.getStringExtra("pass")
    }


    private fun setOnBtnClickListner() {

        btn_nickname_act_next.isEnabled = false

        // 닉네임 중복체크    띄어쓰기 안막음
        btn_nickname_act_check_duplicate_off.setOnClickListener {

            input_nickname = et_nickname_act_nickname.text.toString()

            if (!input_nickname.equals("")) {

                networkService.getCheckDuplicateNickRequest("application/json", input_nickname)
                    .enqueue(object : Callback<GetCheckDuplicateNickResponseData> {
                        override fun onResponse(
                            call: Call<GetCheckDuplicateNickResponseData>,
                            response: Response<GetCheckDuplicateNickResponseData>
                        ) {
                            Log.v("malibin debug", "서버통신성공 : " + response.body().toString())
                            if (response.body()!!.status == 200) {
//                               이건 서버통신 성공해서 중복체크가 성공적으로 완료될때
                                tv_nickname_act_canusenickname.visibility = View.VISIBLE
                                tv_nickname_act_canusenickname.text = "사용 할 수 있는 닉네임 입니다"
                                tv_nickname_act_canusenickname.textColor = Color.BLUE
                                Log.v("malibin debug", "중복체크 성공 : " + response.body().toString())
                                canUseCurrentNick = true
                            }

//                                서버통신실패했을때
                            if (response.body()!!.status == 400) {
                                tv_nickname_act_canusenickname.visibility = View.VISIBLE
                                tv_nickname_act_canusenickname.text = "사용 할 수 없는 닉네임 입니다"
                                tv_nickname_act_canusenickname.textColor = Color.RED

                                canUseCurrentNick = false

                                Log.v("malibin debug", "중복체크 실패 : " + response.body().toString())
                            }
                            completeCheck()
                        }

                        override fun onFailure(call: Call<GetCheckDuplicateNickResponseData>, t: Throwable) {

                        }
                    })
            }
        }

        btn_nickname_act_next.setOnClickListener {

            var nick = et_nickname_act_nickname.text.toString()

            startActivity<AgreementActivity>("id" to id, "pass" to pass, "nick" to nick)
        }
    }

    private fun completeCheck() {

        if (canUseCurrentNick) {

            btn_nickname_act_next.isEnabled = true
            btn_nickname_act_next.backgroundResource = R.color.salmon
            btn_nickname_act_next.textColor = Color.parseColor("#FFFFFF")
        } else {

            btn_nickname_act_next.isEnabled = false
            btn_nickname_act_next.backgroundResource = R.color.white
            btn_nickname_act_next.textColor = Color.parseColor("#909090")
        }

    }


}
