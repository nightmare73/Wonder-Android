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
import java.text.SimpleDateFormat
import java.util.*

class OrderHistoryDetailActivity : AppCompatActivity() {

    val tmp_token =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJEb0lUU09QVCIsInVzZXJfaWR4IjoxfQ.xmbvRqaMuYnGvtPaV_Lw7HorI5blZHlpT7WQgo5ybvM"

    var orderIdx: Int = -1
    var orderTime: String = ""
    var storeName: String = ""
    var totalPrice: Int = 0

    lateinit var orderHistoryDetailRecyclerViewAdapter: OrderHistoryDetailRecyclerViewAdapter

    // 보미 서버 통신
    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history_detail)

        variableInit()
        viewInit()
    }

    private fun variableInit() {

        orderIdx = intent.getIntExtra("orderIdx", -1)
        orderTime = intent.getStringExtra("orderTime")
        storeName = intent.getStringExtra("storeName")

        networkService.getOrderDetailListRequest("application/json", tmp_token, orderIdx).enqueue(object :
            Callback<GetOrderDetailListResponseData> {
            override fun onFailure(call: Call<GetOrderDetailListResponseData>, t: Throwable) {
                toast("서버 통신에 실패하였습니다.")
            }

            override fun onResponse(
                call: Call<GetOrderDetailListResponseData>,
                response: Response<GetOrderDetailListResponseData>
            ) {
                try {
                    setRecyclerView(response)

                    getTotalPrice(response)
                } catch (e: Exception) {
                    toast("서버에 데이터가 존재하지 않습니다.")
                }
            }

        })
    }

    private fun setRecyclerView(response: Response<GetOrderDetailListResponseData>) {

        Log.v("Malibin Debug", "서버에서 온 GetOrderDetailListResponseData : \n" + response.body().toString())
        orderHistoryDetailRecyclerViewAdapter = OrderHistoryDetailRecyclerViewAdapter(
            applicationContext,
            response.body()!!.data.orderDetailList
        )
        rv_order_his_detail_list.adapter = orderHistoryDetailRecyclerViewAdapter
        rv_order_his_detail_list.layoutManager = LinearLayoutManager(applicationContext)

    }

    private fun getTotalPrice(response: Response<GetOrderDetailListResponseData>) {

        for (data in response.body()!!.data.orderDetailList) {
            totalPrice += data.totalPrice
        }
        tv_order_his_detail_act_total.text = totalPrice.toString() + "원"
    }

    private fun viewInit() {

        var date: Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(orderTime)
        orderTime = SimpleDateFormat("yyyy.MM.dd  MM:ss").format(date)

        tv_order_his_detail_act_ordernum.text = orderIdx.toString()
        tv_order_his_detail_act_time.text = orderTime
        tv_order_his_detail_act_storename.text = storeName

        btn_order_his_detail_act_back.setOnClickListener {
            finish()
        }
    }
}
