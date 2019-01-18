package com.wonder.bring.MainFragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wonder.bring.Adapter.CartListRecyclerViewAdapter
import com.wonder.bring.MainActivity
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.NetworkService

import com.wonder.bring.R
import com.wonder.bring.Util.Cart
import com.wonder.bring.db.CartData
import com.wonder.bring.db.SharedPreferenceController
import kotlinx.android.synthetic.main.fragment_cart.*


class CartFragment : Fragment() {

    companion object {
        private var instance: CartFragment? = null
        @Synchronized
        fun getInstance(): CartFragment {
            if (instance == null) {
                instance = CartFragment().apply {
                    arguments = Bundle().apply {
                        //putSerializable("data", data)
                    }
                }
            }
            return instance!!
        }
    }

    lateinit var cartListRecyclerViewAdapter: CartListRecyclerViewAdapter

    var cartList: ArrayList<CartData> = ArrayList()

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_cart, container, false)


        return view

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        btn_cart_frag_login.setOnClickListener {

            (activity as MainActivity).callLoginAct()
        }
    }

    private fun setRecyclerView() {

        cartList.clear()

        val userId: String = SharedPreferenceController.getId(activity!!.applicationContext)

        if (userId.equals("")) {
            //로그인이 안되어있는 경우

        } else {
            //로그인 되어있는경우
            cartList = Cart(activity!!.applicationContext).loadCartData(userId)

            Log.v("Malibin Debug", "장바구니 정보 불러온거 : " + cartList.toString())

            cartListRecyclerViewAdapter = CartListRecyclerViewAdapter(activity!!, cartList)
            rv_cart_frag_list.adapter = cartListRecyclerViewAdapter
            rv_cart_frag_list.layoutManager = LinearLayoutManager(activity)

        }

    }

    fun viewToggle(isLogin: Boolean) {

        if (isLogin) {
            setRecyclerView()
            rl_cart_frag_login.visibility = View.VISIBLE
            rl_cart_frag_logout.visibility = View.GONE
        } else {
            rl_cart_frag_login.visibility = View.GONE
            rl_cart_frag_logout.visibility = View.VISIBLE
        }

    }

    fun refreshTotalPrice(cost: Int){

        tv_cart_frag_total_price.text = (cost.toString() + "원")
    }

}

