package com.wonder.bring.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.wonder.bring.R
import com.wonder.bring.data.CartListData
import java.lang.IndexOutOfBoundsException


class CartListRecyclerViewAdapter(val ctx: Context, val dataList: ArrayList<CartListData>) :
    RecyclerView.Adapter<CartListRecyclerViewAdapter.Holder>(){


    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tv_store_name.text = dataList[position].store_name
        holder.tv_menu_name.text = dataList[position].menu_name
        holder.tv_menu_price.text = dataList[position].menu_price.toString()+"원"
        holder.tv_menu_quantity.text = dataList[position].menu_quantity.toString()
        holder.tv_menu_size.text = dataList[position].menu_size
        holder.tv_menu_request.text=dataList[position].menu_request

        holder.btn_delete.setOnClickListener {
            // 한개 삭제
            try{
                dataList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position,dataList.size)
            }catch (e: IndexOutOfBoundsException){
                Log.e("item 제거 중에 오류 뜸!!",e.toString())
            }
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun addNewItem(cartListData: CartListData){     //추가하는거 여기 adapter쪽에서도 붙여도 된다.
        val positon : Int = dataList.size
        dataList.add(cartListData)
        notifyItemInserted(positon)
    }

    // 들어갈 item에 뷰를 붙이는것
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_cart,parent,false)
        return Holder(view);
    }


    // inner class : 어차피 이 클래스에서는 쓰는 거이기 때문에..
    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var tv_store_name: TextView = itemView.findViewById(R.id.tv_cart_item_store_name)
        var tv_menu_name: TextView = itemView.findViewById(R.id.tv_cart_item_menu_name)
        var tv_menu_size: TextView = itemView.findViewById(R.id.tv_cart_item_menu_size)
        var tv_menu_price: TextView = itemView.findViewById(R.id.tv_cart_item_menu_price)
        var tv_menu_quantity: TextView = itemView.findViewById(R.id.tv_cart_item_menu_quantity)
        var tv_menu_request:TextView=itemView.findViewById(R.id.tv_cart_item_menu_request)

        var btn_delete: Button = itemView.findViewById(R.id.btn_cart_item_delete)
//        var btn_checkbox: CheckBox = itemView.findViewById(R.id.btn_cart_item_checkbox)
//        var btn_minor : Button = itemView.findViewById(R.id.btn_cart_item_minor_icon)
//        var btn_plus : Button = itemView.findViewById(R.id.btn_cart_item_plus_icon)
    }



}