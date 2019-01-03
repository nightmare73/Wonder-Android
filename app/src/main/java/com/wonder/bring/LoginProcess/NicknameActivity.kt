package com.wonder.bring.LoginProcess

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.Get.GetCheckDuplicateIdResponseData
import com.wonder.bring.Network.NetworkService
import com.wonder.bring.R
import kotlinx.android.synthetic.main.activity_nickname.*
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textColor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NicknameActivity : AppCompatActivity() {

    var input_nickname : String = ""

    // 통신을 위한 변수
    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nickname)
        setOnBtnClickListner()

    }


    private fun setOnBtnClickListner(){


//        입력이 안됬을 때 넘어가지 않도록 다 막아놔야하는데 1. 중복체크 이후로만 작동되게 or 색이 변경된 다음 버튼( 다음버튼이 중복체크 이후에만 색이변경되니까!)를 클릭했을때만
        btn_nickname_act_next.setOnClickListener {
            startActivity<AgreementActivity>()
        }

                    // 닉네임 중복체크    띄어쓰기 안막음
            btn_nickname_act_check_duplicate_off.setOnClickListener {

                input_nickname = et_nickname_act_nickname.text.toString()

                if (!input_nickname.equals("")) {

                    networkService.getCheckDuplicateIdRequest("application/json", input_nickname)
                        .enqueue(object : Callback<GetCheckDuplicateIdResponseData> {
                            override fun onResponse(
                                call: Call<GetCheckDuplicateIdResponseData>,
                                response: Response<GetCheckDuplicateIdResponseData>
                            ) {
                                Log.v("malibin debug", "서버통신성공 : " + response.body().toString())
                                if (response.body()!!.status == 200) {
//                               이건 서버통신 성공해서 중복체크가 성공적으로 완료될때
                                    tv_nickname_act_canusenickname.visibility = View.VISIBLE
                                    tv_nickname_act_canusenickname.text = "사용 할 수 있는 닉네임 입니다"
                                    tv_nickname_act_canusenickname.textColor = Color.BLUE
                                    Log.v("malibin debug", "중복체크 성공 : " + response.body().toString())
                                }

//                                서버통신실패했을때
                                if (response.body()!!.status == 400) {
                                    tv_nickname_act_canusenickname.visibility = View.VISIBLE
                                    tv_nickname_act_canusenickname.text = "사용 할 수 없는 닉네임 입니다"
                                    tv_nickname_act_canusenickname.textColor = Color.RED

                                    Log.v("malibin debug", "중복체크 실패 : " + response.body().toString())
                                }
                            }

                            override fun onFailure(call: Call<GetCheckDuplicateIdResponseData>, t: Throwable) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                        })

                }
            }


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
