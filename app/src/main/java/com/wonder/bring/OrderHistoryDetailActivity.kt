package com.wonder.bring

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.wonder.bring.Adapter.OrderHistoryDetailRecyclerViewAdapter
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.Get.GetOrderDetailListResponseData
import com.wonder.bring.Network.NetworkService
import kotlinx.android.synthetic.main.activity_order_history_detail.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderHistoryDetailActivity : AppCompatActivity() {

    val tmp_token =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJEb0lUU09QVCIsInVzZXJfaWR4IjoxfQ.xmbvRqaMuYnGvtPaV_Lw7HorI5blZHlpT7WQgo5ybvM"

    var orderIdx: Int = -1

    lateinit var orderHistoryDetailRecyclerViewAdapter: OrderHistoryDetailRecyclerViewAdapter

    // 보미 서버 통신
    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history_detail)

        variableInit()
    }

    private fun variableInit() {
        orderIdx = intent.getIntExtra("orderIdx", -1)

        networkService.getOrderDetailListRequest("application/json", tmp_token, orderIdx).enqueue(object :
            Callback<GetOrderDetailListResponseData> {
            override fun onFailure(call: Call<GetOrderDetailListResponseData>, t: Throwable) {
                toast("서버 통신에 실패하였습니다.")
            }

            override fun onResponse(
                call: Call<GetOrderDetailListResponseData>,
                response: Response<GetOrderDetailListResponseData>
            ) {

                Log.v("Malibin Debug", "서버에서 온 GetOrderDetailListResponseData : \n" + response.body().toString())
                orderHistoryDetailRecyclerViewAdapter = OrderHistoryDetailRecyclerViewAdapter(
                    applicationContext,
                    response.body()!!.data.orderDetailList
                )
                rv_order_his_detail_list.adapter = orderHistoryDetailRecyclerViewAdapter
                rv_order_his_detail_list.layoutManager = LinearLayoutManager(applicationContext)
            }

        })
    }


    private fun setRecyclerView() {


    }
}
