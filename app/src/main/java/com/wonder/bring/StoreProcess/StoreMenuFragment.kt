package com.wonder.bring.StoreProcess

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.wonder.bring.Adapter.MenuRecyclerViewAdapter
import com.wonder.bring.Network.Get.GetMenuListResponse
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.NetworkService

import com.wonder.bring.R
import com.wonder.bring.data.MenuListData
import kotlinx.android.synthetic.main.fragment_store_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StoreMenuFragment : Fragment() {


    lateinit var menuRecyclerViewAdapter: MenuRecyclerViewAdapter
    lateinit var requestManager : RequestManager


    val listDataList : ArrayList<MenuListData> by lazy {
        ArrayList<MenuListData>()
    }

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_store_menu, container, false)


        getResponse()

        return view;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setRecyclerView()
    }



    private fun getResponse(){
        //todo: 앞에 지도에서 서버에서 stor_idx줄것이다 .. 그게 이 액티비티로 값을 전달시켜서 다시 해야함 // 일단은 임의의 값인 10으로 넣고 진행함

        val getMenuListResponse = networkService.getMenuListResponse("application/json",10)
        getMenuListResponse.enqueue(object : Callback<GetMenuListResponse>{

            override fun onResponse(call: Call<GetMenuListResponse>, response: Response<GetMenuListResponse>) {
                if (response.isSuccessful){

                    var temp : ArrayList<MenuListData> = response.body()!!.data.menuList

                    if (temp.size > 0){
                        val position = menuRecyclerViewAdapter.itemCount
                        menuRecyclerViewAdapter.listDataList.addAll(temp)
                        menuRecyclerViewAdapter.notifyItemInserted(position)

                    }
                }

            }

            override fun onFailure(call: Call<GetMenuListResponse>, t: Throwable) {
                Log.e("Menu list fail", t.toString())
            }


        })
    }

    private fun setRecyclerView(){

        menuRecyclerViewAdapter = MenuRecyclerViewAdapter(activity!!,listDataList)
        rv_store_menu_frag_menu_list.adapter = menuRecyclerViewAdapter
        rv_store_menu_frag_menu_list.layoutManager = LinearLayoutManager(activity)

    }

}
