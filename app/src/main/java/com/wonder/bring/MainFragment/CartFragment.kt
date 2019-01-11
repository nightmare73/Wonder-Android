package com.wonder.bring.MainFragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wonder.bring.Adapter.CartListRecyclerViewAdapter
import com.wonder.bring.LoginProcess.LoginActivity
import com.wonder.bring.MainActivity
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.NetworkService

import com.wonder.bring.R
import com.wonder.bring.data.CartListData
import com.wonder.bring.db.SharedPreferenceController
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart_no.view.*
import kotlinx.android.synthetic.main.fragment_login_no.view.*
import org.jetbrains.anko.support.v4.startActivity


class CartFragment : Fragment(){

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

        // 로그인 여부 파악 : case1) 로그인 앙대어 있으면
//        if(!SharedPreferenceController.getAuthorization(activity!!).isNotEmpty()){
//
//            view=inflater.inflate(R.layout.fragment_login_no,container,false)
//            view.btn_login_no_frag_goto_login.setOnClickListener {
//                startActivity<LoginActivity>()
//            }
//
//            //로그인 여부파악 : case2) 로그인 되어있는 경우
//        }else{
//
            // todo: if( 장바구니에 아무것도 없다면) :

//        ifview=inflater.inflate(R.layout.fragment_cart_no,container,false)
            // todo: else(장바구니에 1개라도 있다면)
            // 원래 view 뿌려주자
//
//
//
//        }


        return view

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        setRecyclerView()

    }

    private fun setRecyclerView(){

        // 임시데이터
        listDataList.add(CartListData
            ("커피나무",
            "아메리카노ICE",
            "Regular",
            5000,
            1,
            "위에 생크림 올려주세요"
            )
        )

        listDataList.add(CartListData
            ("커피나무",
            "아메리카노ICE",
            "Regular",
            5000,
            1,
            "위에 생크림 올려주세요"
        ))


        listDataList.add(CartListData
            ("커피나무",
            "아메리카노ICE",
            "Regular",
            5000,
            1,
            "위에 생크림 올려주세요"
        )
        )



        cartListRecyclerViewAdapter= CartListRecyclerViewAdapter(activity!!,listDataList)
        rv_cart_frag_list.adapter=cartListRecyclerViewAdapter
        rv_cart_frag_list.layoutManager=LinearLayoutManager(activity)

    }


}

