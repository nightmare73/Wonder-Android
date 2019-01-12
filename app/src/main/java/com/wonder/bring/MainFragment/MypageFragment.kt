package com.wonder.bring.MainFragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.wonder.bring.LoginProcess.LoginActivity
import com.wonder.bring.MainActivity
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.Get.GetMypageResponse
import com.wonder.bring.Network.NetworkService
import com.wonder.bring.Network.Post.PostLogInResponse

import com.wonder.bring.R
import com.wonder.bring.db.SharedPreferenceController
import kotlinx.android.synthetic.main.fragment_login_no.view.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import kotlinx.android.synthetic.main.fragment_mypage.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 통신을 위한 변수
val networkService: NetworkService by lazy {
    ApplicationController.instance.networkService
}

class MypageFragment : Fragment() {

    companion object {
        private var instance: MypageFragment? = null
        @Synchronized
        fun getInstance(): MypageFragment {
            if (instance == null) {
                instance = MypageFragment().apply {
                    arguments = Bundle().apply {
                        //putSerializable("data", data)
                    }
                }
            }
            return instance!!
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_mypage, container, false)




        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getResponse()


        btn_mypage_frag_logout.setOnClickListener {
            Log.v("Malibin Debug", "로그아웃버튼 눌림")
            SharedPreferenceController.setAuthorization(activity!!.applicationContext,"")

            (activity as MainActivity).settingView(false)
        }



        btn_mypage_frag_login.setOnClickListener {

            (activity as MainActivity).callLoginAct()
        }

        tv_mypage_act_nickname.text = (activity as MainActivity).nick
    }

    fun viewToggle(isLogin: Boolean) {

        if (isLogin) {
            rl_mypage_frag_login.visibility = View.VISIBLE
            rl_mypage_frag_logout.visibility = View.GONE
        } else {
            rl_mypage_frag_login.visibility = View.GONE
            rl_mypage_frag_logout.visibility = View.VISIBLE
        }

    }

    fun getResponse() {

        //토큰 받아와서
        var token = SharedPreferenceController.getAuthorization(activity!!)

        //통신 시작
        val getMypageResponse: Call<GetMypageResponse> = networkService.getMypageResponse(token)


        getMypageResponse.enqueue(object : Callback<GetMypageResponse> {
            override fun onResponse(call: Call<GetMypageResponse>, response: Response<GetMypageResponse>) {
                var body = response!!.body()
                tv_mypage_act_nickname.text=body!!.data.nick


                val requestOptions = RequestOptions()
                Glide.with(activity!!)
                    .setDefaultRequestOptions(requestOptions)
                    .load(body!!.data.photoUrl)
                    .thumbnail(0.5f)
                    .into(iv_mypage_frag_user_image)
            }


        override fun onFailure(call: Call<GetMypageResponse>, t: Throwable) { // 통신 실패
            Log.d("서버연결 실패", t.toString())
        }
    })

    }


}
