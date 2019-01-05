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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StoreInfoFragment : Fragment() {

    // 통신을 위한 변수
    val networkService : NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_store_info, container, false)

        getResponse()

       return view;
    }


    fun getResponse(){  // 매장
        //todo : storenumber넘어오면 넣어서 통신다시 해봐야 함
        val getStoreInfoResponse = networkService.getStoreInfoResponse("application/json",10)
        getStoreInfoResponse.enqueue(object : Callback<GetStoreInfoResponse> {

            override fun onResponse(call: Call<GetStoreInfoResponse>, response: Response<GetStoreInfoResponse>) {
                if (response.isSuccessful){
                    var body = response!!.body()

                    if(body!!.message.equals("매장 상세 정보 조회 성공")){

                        Log.d("매장 상세 정보 조회 성공","매장 상세 정보 조회 성공")
                        tv_store_info_frag_time.text=body!!.data.time
                        tv_store_info_frag_hoilday.text=body!!.data.breakDays
                        tv_store_info_frag_tel.text=body!!.data.number

                    }else{
                        Log.d("매장 상세 정보 조회 실패","매장 상세 정보 조회 실패")
                    }

                }

            }

            override fun onFailure(call: Call<GetStoreInfoResponse>, t: Throwable) {
                Log.e("Menu list fail", t.toString())
            }


        })
    }


}
