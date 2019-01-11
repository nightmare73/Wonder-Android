package com.wonder.bring.MainFragment

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wonder.bring.Adapter.OrderListRecyclerViewAdapter
import com.wonder.bring.LoginProcess.LoginActivity
import com.wonder.bring.MainActivity
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.Get.GetMenuListResponse
import com.wonder.bring.Network.Get.GetOrderListResponse
import com.wonder.bring.Network.NetworkService

import com.wonder.bring.R
import com.wonder.bring.data.MenuListData
import com.wonder.bring.data.OrderListData
import com.wonder.bring.db.SharedPreferenceController
import kotlinx.android.synthetic.main.activity_store.*
import kotlinx.android.synthetic.main.fragment_orderhistory.*
import kotlinx.android.synthetic.main.rv_item_orderlist.*
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderhistoryFragment : Fragment(), View.OnClickListener {

    companion object {
        private var instance: OrderhistoryFragment? = null
        @Synchronized
        fun getInstance(): OrderhistoryFragment {
            if (instance == null) {
                instance = OrderhistoryFragment().apply {
                    arguments = Bundle().apply {
                        //putSerializable("data", data)
                    }
                }
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


//        // case1 :  로그인 앙대어 있으면
//        if(!SharedPreferenceController.getAuthorization(activity!!).isNotEmpty()){
//
//            view=inflater.inflate(R.layout.fragment_login_no,container,false)
//            view.btn_login_no_frag_goto_login.setOnClickListener {
//                startActivity<LoginActivity>()
////                activity!!.overridePendingTransition(R.anim.slide_in_up,0)
//            }
//
//        //case2 : 로그인 되어있는 경우
//
//        }else{
//
//
//
//        }


        return view
    }

    override fun onClick(v: View?) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setBtnClickListener()
        setRecyclerView()

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

        // todo: 통신을 위해서 잠깐 tmp_token사용한것
        // todo: sharedpreference에서 내 토큰 꺼내와서 다시 통신 해보야함.
        //val tmp_token =
        //     "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJEb0lUU09QVCIsInVzZXJfaWR4IjoxfQ.xmbvRqaMuYnGvtPaV_Lw7HorI5blZHlpT7WQgo5ybvM"
        // val getOrderListResponse = networkService.getOrderListResponse(tmp_token)

        val token = SharedPreferenceController.getAuthorization(activity!!.applicationContext)
        val getOrderListResponse = networkService.getOrderListResponse(token)

        getOrderListResponse.enqueue(object : Callback<GetOrderListResponse> {

            override fun onResponse(call: Call<GetOrderListResponse>, response: Response<GetOrderListResponse>) {
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

                            if (temp.size > 0) {
                                val position = orderRecyclerViewAdapter.itemCount
                                orderRecyclerViewAdapter.dataList.addAll(temp)
                                orderRecyclerViewAdapter.notifyItemInserted(position)

                            }
                        }
                        // case 2
                        "주문내역이 존재하지 않습니다." -> {
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
