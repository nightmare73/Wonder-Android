package com.wonder.bring

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import com.wonder.bring.LoginProcess.SignupActivity
import com.wonder.bring.StoreFragment.StoreInfoFragment
import com.wonder.bring.StoreFragment.StoreMenuFragment
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_store.*
import org.jetbrains.anko.startActivity

class StoreActivity : AppCompatActivity(), View.OnClickListener {

    private val FRAGMENT1 = 1
    private val FRAGMENT2 = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)


        callFragment(FRAGMENT1)
        btn_store_act_menu!!.setOnClickListener(this)
        btn_store_act_info.setOnClickListener(this)

    }


        override fun onClick(v: View) {
            when (v.id) {
            R.id.btn_store_act_menu ->{
                tv_store_act_menu!!.setTextColor(Color.parseColor("#000000"))
                tv_store_act_info!!.setTextColor(Color.parseColor("#909090"))
                iv_store_act_menu.visibility=View.VISIBLE
                iv_store_act_info.visibility=View.INVISIBLE
                callFragment(FRAGMENT1)
            }


            R.id.btn_store_act_info -> {
                tv_store_act_info!!.setTextColor(Color.parseColor("#000000"))
                tv_store_act_menu!!.setTextColor(Color.parseColor("#909090"))
                iv_store_act_info.visibility=View.VISIBLE
                iv_store_act_menu.visibility=View.INVISIBLE
                callFragment(FRAGMENT2)
            }
        }
    }



    private fun callFragment(frament_no: Int) {

        when (frament_no) {
            1 -> { // '프래그먼트1' 호출
                val store_menu_fragment : StoreMenuFragment= StoreMenuFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fl_store_act_framelayout,store_menu_fragment).commit()
            }

            2 -> { // '프래그먼트2' 호출
                val store_info_fragment : StoreInfoFragment = StoreInfoFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fl_store_act_framelayout, store_info_fragment).commit()
            }
        }

    }


}
