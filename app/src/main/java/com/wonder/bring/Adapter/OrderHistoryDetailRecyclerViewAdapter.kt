package com.wonder.bring.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wonder.bring.Network.Get.OtherDataClasses.OrderDetail
import com.wonder.bring.R
import com.wonder.bring.SizeConvertor

class OrderHistoryDetailRecyclerViewAdapter(val ctx: Context, val dataList: ArrayList<OrderDetail>) :
    RecyclerView.Adapter<OrderHistoryDetailRecyclerViewAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.item_order_detail, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tv_name.text = dataList[position].name
        holder.tv_amountSize.text =
                ("(" + SizeConvertor.parseSizeString(dataList[position].size) + " / " + dataList[position].orderCount + "개")
        holder.tv_request.text  = dataList[position].memo
        holder.tv_cost.text = dataList[position].totalPrice.toString()
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_name: TextView = itemView.findViewById(R.id.tv_item_order_detail_name)
        var tv_amountSize: TextView = itemView.findViewById(R.id.tv_item_order_detail_size_amount)
        var tv_request: TextView = itemView.findViewById(R.id.tv_item_order_detail_request)
        var tv_cost: TextView = itemView.findViewById(R.id.tv_item_order_detail_cost)
    }
}