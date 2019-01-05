package com.wonder.bring.StoreProcess

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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

class StoreActivity : AppCompatActivity(), View.OnClickListener {

    private val FRAGMENT1 = 1
    private val FRAGMENT2 = 2
    lateinit var requestManager : RequestManager

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }


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

    private fun getResponse(v : View) {
        val getMenuListResponse = networkService.getMenuListResponse("application/json", 10)

        getMenuListResponse.enqueue(object : Callback<GetMenuListResponse> {

            override fun onResponse(call: Call<GetMenuListResponse>, response: Response<GetMenuListResponse>) {
                if (response.isSuccessful) {

                    var body = response!!.body()
                    toast("메뉴 리스트 조회 성공")

                    if (body!!.message.equals("메뉴 리스트 조회 성공")) {

                        var store_name = body!!.data.name
                        var store_address = body!!.data.address

                        tv_store_act_store_name.text = store_name
                        tv_store_act_store_address.text = store_address


                        requestManager.load(body!!.data.bgPhotoUrl)

                    } else {
                    toast("메뉴 리스트 조회 실패")
                    }


                }

            }

            override fun onFailure(call: Call<GetMenuListResponse>, t: Throwable) {
                Log.e("Menu list fail", t.toString())
            }


        })
    }

}
