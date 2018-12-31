package com.wonder.bring.StoreFragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wonder.bring.Adapter.MenuRecyclerViewAdapter
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.NetworkService

import com.wonder.bring.R
import com.wonder.bring.data.MenuData
import kotlinx.android.synthetic.main.fragment_store_menu.*


class StoreMenuFragment : Fragment() {


    lateinit var menuRecyclerViewAdapter: MenuRecyclerViewAdapter

    val dataList : ArrayList<MenuData> by lazy {
        ArrayList<MenuData>()
    }

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store_menu, container, false)

//        setOnBtnClickListener()
//        getBoardListResponse()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setRecyclerView()
    }

    private fun setRecyclerView(){

        // 임시데이터
        // todo: 서버통신하면 다시 바꿔야함
        var dataList : ArrayList<MenuData> = ArrayList()

        dataList.add(MenuData("아메리카노",5000))
        dataList.add(MenuData("아메리카노",5000))
        dataList.add(MenuData("아메리카노",5000))
        dataList.add(MenuData("아메리카노",5000))
        dataList.add(MenuData("아메리카노",5000))
        dataList.add(MenuData("아메리카노",5000))


        menuRecyclerViewAdapter = MenuRecyclerViewAdapter(activity!!, dataList)
        rv_store_menu_frag_menu_list.adapter = menuRecyclerViewAdapter
        rv_store_menu_frag_menu_list.layoutManager = LinearLayoutManager(activity)

    }

}
