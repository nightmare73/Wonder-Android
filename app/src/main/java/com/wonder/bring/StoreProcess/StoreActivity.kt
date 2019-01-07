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

    private val FRAGMENT1 = 1
    private val FRAGMENT2 = 2

    private var mLastClickTime: Long = 0  // 연속 클릭을 막기 위한 변수

    lateinit var requestManager : RequestManager
    private val TAG = StoreActivity::class.java!!.getSimpleName()

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        requestManager = Glide.with(this)

        callFragment(FRAGMENT1)
        btn_store_act_menu!!.setOnClickListener(this)
        btn_store_act_info.setOnClickListener(this)


        // todo: 앞에 홈에서 매장 이름, 매장 주소 가지고 와서 집어넣어야 함.
        getResponse()

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

    private fun getResponse() {
        val getMenuListResponse = networkService.getMenuListResponse("application/json", 10)

//        getMenuListResponse.enqueue(object : Callback<GetMenuListResponse> {
//
//            override fun onResponse(call: Call<GetMenuListResponse>, response: Response<GetMenuListResponse>) {
//                if (response.isSuccessful) {
//
//                    var body = response!!.body()
//
//                    if (body!!.message.equals("메뉴 리스트 조회 성공")) {
//                        Log.d(TAG,"MenuList Success")
//                        // 이미지 로드
//                        requestManager.load(body!!.data.bgPhotoUrl).into(iv_store_act_store_bgphoto)
//
//                    } else
//                        Log.d(TAG,"MenuList Fail")
//                }
//
//            }
//
//            override fun onFailure(call: Call<GetMenuListResponse>, t: Throwable) {
//                Log.e("Menu list fail", t.toString())
//            }
//        })
    }

}
