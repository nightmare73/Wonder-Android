package com.wonder.bring.MainFragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wonder.bring.LoginProcess.LoginActivity

import com.wonder.bring.R
import com.wonder.bring.db.SharedPreferenceController
import kotlinx.android.synthetic.main.fragment_login_no.view.*
import org.jetbrains.anko.support.v4.startActivity


class OrderhistoryFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_orderhistory, container, false)

        // case1 :  로그인 앙대어 있으면
        if(!SharedPreferenceController.getAuthorization(activity!!).isNotEmpty()){

            view=inflater.inflate(R.layout.fragment_login_no,container,false)
            view.btn_login_no_frag_goto_login.setOnClickListener {
                startActivity<LoginActivity>()
            }

        //case2 : 로그인 되어있는 경우
        }else{


        }



        return view
    }


}
