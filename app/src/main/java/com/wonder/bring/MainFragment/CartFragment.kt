package com.wonder.bring.MainFragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wonder.bring.Adapter.CartListRecyclerViewAdapter
import com.wonder.bring.MainActivity
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.NetworkService

import com.wonder.bring.R
import com.wonder.bring.data.CartListData
import kotlinx.android.synthetic.main.fragment_cart.*


class CartFragment : Fragment(){

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


    private val TAG = CartFragment::class.java!!.getSimpleName()

    lateinit var cartListRecyclerViewAdapter: CartListRecyclerViewAdapter


    val listDataList : ArrayList<CartListData> by lazy {
        ArrayList<CartListData>()
    }

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view=inflater.inflate(R.layout.fragment_cart, container, false)


        return view

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        setRecyclerView()

        btn_cart_frag_login.setOnClickListener {

            (activity as MainActivity).callLoginAct()
        }
    }

    private fun setRecyclerView(){

//        // 임시데이터
//        listDataList.add(CartListData
//            ("커피나무",
//            "아메리카노ICE",
//            "Regular",
//            5000,
//            1,
//            "위에 생크림 올려주세요"
//            )
//        )
//
//        listDataList.add(CartListData
//            ("커피나무",
//            "아메리카노ICE",
//            "Regular",
//            5000,
//            1,
//            "위에 생크림 올려주세요"
//        ))
//
//
//        listDataList.add(CartListData
//            ("커피나무",
//            "아메리카노ICE",
//            "Regular",
//            5000,
//            1,
//            "위에 생크림 올려주세요"
//        )
//        )
//


//        cartListRecyclerViewAdapter= CartListRecyclerViewAdapter(activity!!,listDataList)
//        rv_cart_frag_list.adapter=cartListRecyclerViewAdapter
//        rv_cart_frag_list.layoutManager=LinearLayoutManager(activity)

    }
    fun viewToggle(isLogin: Boolean) {

        if (isLogin) {
            rl_cart_frag_login.visibility = View.VISIBLE
            rl_cart_frag_logout.visibility = View.GONE
        } else {
            rl_cart_frag_login.visibility = View.GONE
            rl_cart_frag_logout.visibility = View.VISIBLE
        }

    }

}

