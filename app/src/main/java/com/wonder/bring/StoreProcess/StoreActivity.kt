package com.wonder.bring.StoreProcess

import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.wonder.bring.Network.Get.GetMenuListResponse
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.NetworkService
import com.wonder.bring.R
import kotlinx.android.synthetic.main.activity_store.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.graphics.drawable.Drawable
import android.widget.ImageView


class StoreActivity : AppCompatActivity(), View.OnClickListener {

    //-1이면 서버통신 동작 못하게 막아놓기 -1서버로 가면 안된다!!
    private var storeIdx: Int = -1
    private var bundle = Bundle(1)

    private val FRAGMENT1 = 1
    private val FRAGMENT2 = 2

    private var mLastClickTime: Long = 0  // 연속 클릭을 막기 위한 변수

    lateinit var requestManager: RequestManager
    private val TAG = StoreActivity::class.java!!.getSimpleName()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        requestManager = Glide.with(this)

        viewInit()

    }

    private fun viewInit(){
        btn_store_act_menu!!.setOnClickListener(this)
        btn_store_act_info.setOnClickListener(this)

        //storeIdx에 홈프래그먼트에서 보내준 값을 저장한다.
        storeIdx = intent.getIntExtra("storeIdx", -1)

        //다른 프래그먼트들에게 전달해줄 storeIdx값을 번들에다가 집어넣는다
        bundle.putInt("storeIdx", storeIdx)

        callFragment(FRAGMENT1)

        val storeName: String = intent.getStringExtra("storeName")
        val storeAdress: String = intent.getStringExtra("storeAdress")

        tv_store_act_store_name.text = storeName
        tv_store_act_store_address.text = storeAdress

        btn_store_act_back.setOnClickListener {
            finish()
        }
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_store_act_menu -> {
                tv_store_act_menu!!.setTextColor(Color.parseColor("#000000"))
                tv_store_act_info!!.setTextColor(Color.parseColor("#909090"))
                iv_store_act_menu.visibility = View.VISIBLE
                iv_store_act_info.visibility = View.INVISIBLE
                callFragment(FRAGMENT1)
            }


            R.id.btn_store_act_info -> {
                tv_store_act_info!!.setTextColor(Color.parseColor("#000000"))
                tv_store_act_menu!!.setTextColor(Color.parseColor("#909090"))
                iv_store_act_info.visibility = View.VISIBLE
                iv_store_act_menu.visibility = View.INVISIBLE
                callFragment(FRAGMENT2)
            }
        }
    }


    private fun callFragment(frament_no: Int) {

        when (frament_no) {
            1 -> { // '프래그먼트1' 호출
                val store_menu_fragment: StoreMenuFragment = StoreMenuFragment()

                store_menu_fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.fl_store_act_framelayout, store_menu_fragment)
                    .commit()
            }

            2 -> { // '프래그먼트2' 호출
                val store_info_fragment: StoreInfoFragment = StoreInfoFragment()

                store_info_fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.fl_store_act_framelayout, store_info_fragment)
                    .commit()
            }
        }
    }

    //프래그먼트에서 서버통신후 이 함수를 실행시켜 액티비티의 이미지뷰 갱신
    fun changeBackgroundImage(imageUrl: String){
        Glide.with(this).load(imageUrl).into(iv_store_act_store_bgphoto)
    }

}
