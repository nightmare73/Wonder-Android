package com.wonder.bring.MainFragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Checkable
import com.wonder.bring.LoginProcess.LoginActivity

import com.wonder.bring.R
import com.wonder.bring.db.SharedPreferenceController
import kotlinx.android.synthetic.main.fragment_cart.view.*
import kotlinx.android.synthetic.main.fragment_login_no.*
import kotlinx.android.synthetic.main.fragment_login_no.view.*
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast


class CartFragment : Fragment(){



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view=inflater.inflate(R.layout.fragment_cart, container, false)

        // 로그인 여부 파악 : case1) 로그인 앙대어 있으면
        if(!SharedPreferenceController.getAuthorization(activity!!).isNotEmpty()){

            view=inflater.inflate(R.layout.fragment_login_no,container,false)
            view.btn_login_no_frag_goto_login.setOnClickListener {
                startActivity<LoginActivity>()
                activity!!.overridePendingTransition(R.anim.slide_in_up,0)
            }

            //로그인 여부파악 : case2) 로그인 되어있는 경우
        }else{

            // todo: if( 장바구니에 아무것도 없다면) :
//            view=inflater.inflate(R.layout.fragment_cart_no,container,false)
            // todo: else(장바구니에 1개라도 있다면)
            // 원래 view 뿌려주자



        }


        return view
    }

}
