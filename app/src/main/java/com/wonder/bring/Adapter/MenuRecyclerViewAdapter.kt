package com.wonder.bring.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.wonder.bring.R
import com.wonder.bring.StoreFragment.StoreMenuFragment
import com.wonder.bring.data.MenuData

class MenuRecyclerViewAdapter(val ctx: Context, val dataList: ArrayList<MenuData>) : RecyclerView.Adapter<MenuRecyclerViewAdapter.Holder>(){

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.menu_name.text=dataList[position].menu_name
        holder.menu_price.text=dataList[position].menu_price.toString()

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun addNewItem(menuData: MenuData){     //추가하는거 여기 adapter쪽에서도 붙여도 된다.
        val positon : Int = dataList.size
        dataList.add(menuData)
        notifyItemInserted(positon)
    }

    // 들어갈 item에 뷰를 붙이는것
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_menu_item,parent,false)
        return Holder(view);
    }


    // inner class : 어차피 이 클래스에서는 쓰는 거이기 때문에..
    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val menu_name: TextView =itemView.findViewById(R.id.tv_rv_menu_item_menu_name) as TextView
        val menu_price : TextView =itemView.findViewById(R.id.tv_rv_menu_item_menu_price) as TextView
//        val whole_btn: RelativeLayout =itemView.findViewById(R.id.btn_menu_item_whole_box) as RelativeLayout
    }

}