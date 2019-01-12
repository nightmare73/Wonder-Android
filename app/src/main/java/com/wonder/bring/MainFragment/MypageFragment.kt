package com.wonder.bring.MainFragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wonder.bring.LoginProcess.LoginActivity
import com.wonder.bring.MainActivity

import com.wonder.bring.R
import com.wonder.bring.db.SharedPreferenceController
import kotlinx.android.synthetic.main.fragment_login_no.view.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import kotlinx.android.synthetic.main.fragment_mypage.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast


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


//        if(!SharedPreferenceController.getAuthorization(activity!!).isNotEmpty()){
////
////            view=inflater.inflate(R.layout.fragment_login_no,container,false)
////            view.btn_login_no_frag_goto_login.setOnClickListener {
////                startActivity<LoginActivity>()
////            }
////
////        }else{
////            view=inflater.inflate(R.layout.fragment_mypage, container, false)
////
////            view.btn_mypage_frag_logout.setOnClickListener {
////
////                // 자동로그인 로직 지우고, 다시 메인으로 가야해.
////                SharedPreferenceController.clearSPC(activity!!)
////                if (activity is MainActivity)
////                    (activity as MainActivity).moveToTab(0)
////
////            }
////
////
////        }


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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

}
