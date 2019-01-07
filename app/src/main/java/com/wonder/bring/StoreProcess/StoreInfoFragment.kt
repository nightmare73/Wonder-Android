package com.wonder.bring.StoreProcess

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wonder.bring.Network.Get.GetStoreInfoResponse
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.NetworkService

import com.wonder.bring.R
import kotlinx.android.synthetic.main.fragment_store_info.*
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StoreInfoFragment : Fragment() {

    private val TAG = StoreInfoFragment::class.java!!.getSimpleName()

    private var storeIdx = -1

    // 통신을 위한 변수
    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_store_info, container, false)

        storeIdx = arguments!!.getInt("storeIdx")
        Log.v("Malibin Debug", "StoreInfoFragment에 넘어온 storeIdx값 : " + storeIdx)

        getResponse()

        return view
    }

    private fun getResponse() {  // 매장

        //storeIdx -1을 서버에 보내면 (서버에 없는 인덱스값을 보내면) 서버 터진대 여기서 막아주자
        if (storeIdx == -1) {
            toast("서버에서 매장 정보를 불러오지 못했습니다.")
        } else {

            val getStoreInfoResponse = networkService.getStoreInfoResponse("application/json", storeIdx)
            getStoreInfoResponse.enqueue(object : Callback<GetStoreInfoResponse> {

                override fun onResponse(call: Call<GetStoreInfoResponse>, response: Response<GetStoreInfoResponse>) {
                    if (response.isSuccessful) {
                        var body = response!!.body()

                        if (body!!.message.equals("매장 상세 정보 조회 성공")) {

                            Log.d(TAG, "매장 상세 정보 조회 성공")
                            tv_store_info_frag_time.text = body!!.data.time
                            tv_store_info_frag_hoilday.text = body!!.data.breakDays
                            tv_store_info_frag_tel.text = body!!.data.number

                        } else {
                            Log.d(TAG, "매장 상세 정보 조회 실패")
                        }
                    }
                }
                override fun onFailure(call: Call<GetStoreInfoResponse>, t: Throwable) {
                    toast("서버에서 매장 정보를 불러오지 못했습니다.")
                    Log.e("Menu list fail", t.toString())
                }
            })
        }
    }


}
