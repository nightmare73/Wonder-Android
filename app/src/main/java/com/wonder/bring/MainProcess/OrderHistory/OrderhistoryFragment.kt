package com.wonder.bring.MainProcess.OrderHistory

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wonder.bring.Adapter.OrderListRecyclerViewAdapter
import com.wonder.bring.MainProcess.MainActivity
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.Get.GetOrderListResponse
import com.wonder.bring.Network.NetworkService

import com.wonder.bring.R
import com.wonder.bring.Network.Get.OtherDataClasses.OrderListData
import com.wonder.bring.Util.SharedPreferenceController
import kotlinx.android.synthetic.main.fragment_orderhistory.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderhistoryFragment : Fragment(), View.OnClickListener {

    companion object {
        private var instance: OrderhistoryFragment? = null
        @Synchronized
        fun getInstance(): OrderhistoryFragment {
            if (instance == null) {
                instance = OrderhistoryFragment()
                    .apply {
                    arguments = Bundle().apply {
                        //putSerializable("data", data)
                    }
                }
                Log.v("Malibin Debug", "OrderhistoryFragment 인스턴스가 생성됨")
            }
            return instance!!
        }
    }


    private val TAG = OrderhistoryFragment::class.java!!.getSimpleName()

    lateinit var orderRecyclerViewAdapter: OrderListRecyclerViewAdapter


    val listDataList: ArrayList<OrderListData> by lazy {
        ArrayList<OrderListData>()
    }

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_orderhistory, container, false)

        Log.v("Malibin Debug", "OrderhistoryFragment  onCreateView 실행")
        return view
    }

    override fun onClick(v: View?) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setBtnClickListener()
        setRecyclerView()
        Log.v("Malibin Debug", "OrderhistoryFragment  onActivityCreated 실행")

    }

    private fun setBtnClickListener() {

        btn_oh_frag_login.setOnClickListener {

            (activity as MainActivity).callLoginAct()
        }
    }


    private fun setRecyclerView() {

        orderRecyclerViewAdapter = OrderListRecyclerViewAdapter(activity!!, listDataList)
        rv_order_frag_list.adapter = orderRecyclerViewAdapter
        rv_order_frag_list.layoutManager = LinearLayoutManager(activity)


    }



    private fun getResponse() {

        val token = SharedPreferenceController.getAuthorization(activity!!.applicationContext)
        val getOrderListResponse = networkService.getOrderListResponse(token)

        getOrderListResponse.enqueue(object : Callback<GetOrderListResponse> {

            override fun onResponse(call: Call<GetOrderListResponse>, response: Response<GetOrderListResponse>) {
                Log.v("Malibin Debug", "orderHistory 응답바디 : " + response.body().toString())

                if (response.isSuccessful) {

                    var body = response!!.body()
                    var temp: ArrayList<OrderListData> = ArrayList()
                    try {
                        temp = response.body()!!.data.orderList
                    } catch (e: Exception) {

                    }

                    when (body!!.message) {

                        //case 1
                        "주문내역 조회 성공" -> {
                            Log.d(TAG, "OrderList Success")
                            tv_orderhistory_frag_nickname.text = body.data.nick

                            (activity as MainActivity).nick = body.data.nick

                            if (temp.size > 0) {
                                //메인액티비티가 종료되고 다시 실행되면 프래그먼트들의 인스턴스들은 그대로 남아있지만 이 통신하는 함수는 메인액티비티 로그인 검사에 의해 실행이 됨.
                                //그래서 주문목록이 누적되는 현상이 발생. 그렇기때문에 데이터리스트를 한번 비워낸다.
                                orderRecyclerViewAdapter.dataList.clear()

                                val position = orderRecyclerViewAdapter.itemCount
                                orderRecyclerViewAdapter.dataList.addAll(temp)
                                orderRecyclerViewAdapter.notifyItemInserted(position)

                            }
                        }
                        // case 2
                        "주문내역이 존재하지 않습니다." -> {
                            tv_orderhistory_frag_nickname.text = body.data.nick
                            //애초에 바디가 널로옴
                            Log.d(TAG, "OrderList S")
                        }
                        // case 3
                        "인증 실패" -> Log.d(TAG, "autorization failed")

                        //case 4
                        "서버 내부 에러" -> Log.e(TAG, "OrderList Failed")

                    }

                } else {
                    Log.e(TAG, "OrderList Failed")
                }

            }

            override fun onFailure(call: Call<GetOrderListResponse>, t: Throwable) {
                Log.e("Order list fail", t.toString())
            }
        })
    }

    fun viewToggle(isLogin: Boolean) {

        if (isLogin) {
            getResponse()
            ll_orderhistory_frag_login.visibility = View.VISIBLE
            ll_orderhistory_frag_logout.visibility = View.GONE
        } else {
            orderRecyclerViewAdapter.dataList.clear()
            ll_orderhistory_frag_login.visibility = View.GONE
            ll_orderhistory_frag_logout.visibility = View.VISIBLE
        }

    }
}
