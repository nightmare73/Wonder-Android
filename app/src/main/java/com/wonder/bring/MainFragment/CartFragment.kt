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
import com.wonder.bring.db.CartData
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


    private val TAG = CartFragment::class.java!!.getSimpleName()

    lateinit var cartListRecyclerViewAdapter: CartListRecyclerViewAdapter


    val listDataList: ArrayList<CartData> by lazy {
        ArrayList<CartData>()
    }

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_cart, container, false)


        return view

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //setTempRecyclerView()

        btn_cart_frag_login.setOnClickListener {

            (activity as MainActivity).callLoginAct()
        }
    }

    private fun setTempRecyclerView() {

        // 임시데이터
        listDataList.add(
            CartData
                (
                6,
                50,
                "https://s3.ap-northeast-2.amazonaws.com/project-bring/d3ae3b9f0f174a2d988fc25b0f4f90d0.jpg",
                "탐앤탐스커피 역삼2호점",
                "카페라떼HOT",
                "위에 생크림 올려주세요",
                2,
                1,
                4700
            )
        )

        listDataList.add(
            CartData
                (
                6,
                53,
                "https://s3.ap-northeast-2.amazonaws.com/project-bring/e2b436458b1e43faa4e9345a8e5967d2.jpg",
                "탐앤탐스커피 역삼2호점",
                "캬라멜마끼아또HOT",
                "해고니가 좋아하는 캬마",
                3,
                2,
                5600
            )
        )

        listDataList.add(
            CartData
                (
                6,
                46,
                "https://s3.ap-northeast-2.amazonaws.com/project-bring/34f56cd7156848f4aedfba20a197d528.jpg",
                "탐앤탐스커피 역삼2호점",
                "소이도그프레즐",
                "간식쓰",
                1,
                4,
                7300
            )
        )


        cartListRecyclerViewAdapter= CartListRecyclerViewAdapter(activity!!,listDataList)
        rv_cart_frag_list.adapter=cartListRecyclerViewAdapter
        rv_cart_frag_list.layoutManager= LinearLayoutManager(activity)

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

