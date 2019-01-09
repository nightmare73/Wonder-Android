package com.wonder.bring.Adapter

import android.content.Context
import org.jetbrains.anko.support.v4.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wonder.bring.OrderHistoryDetailActivity
import com.wonder.bring.R
import com.wonder.bring.data.OrderListData
import org.jetbrains.anko.startActivity


class OrderListRecyclerViewAdapter(val ctx: Context, val dataList: ArrayList<OrderListData>) :
    RecyclerView.Adapter<OrderListRecyclerViewAdapter.Holder>(){


    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tv_order_number.text=dataList[position].orderIdx.toString()
        holder.tv_store_name.text = dataList[position].name
        holder.tv_order_time.text = dataList[position].time

        val requestOptions = RequestOptions()

        when(dataList[position].state){

            0->{// 주문 요청
                Glide.with(ctx)
                    .setDefaultRequestOptions(requestOptions)
                    .load(R.drawable.order_request)
                    .thumbnail(0.5f)
                    .into(holder.iv_flag_state)
            }
            1->{  // 주문 접수
                Glide.with(ctx)
                    .setDefaultRequestOptions(requestOptions)
                    .load(R.drawable.order_allowed)
                    .thumbnail(0.5f)
                    .into(holder.iv_flag_state)
            }
            2-> {  // 제조 완료
                Glide.with(ctx)
                    .setDefaultRequestOptions(requestOptions)
                    .load(R.drawable.order_menu_done)
                    .thumbnail(0.5f)
                    .into(holder.iv_flag_state)
            }
        }


        holder.btn_order_detaild.setOnClickListener {
            ctx.startActivity<OrderHistoryDetailActivity>("orderIdx" to dataList[position].orderIdx)
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun addNewItem(orderListData: OrderListData){     //추가하는거 여기 adapter쪽에서도 붙여도 된다.
        val positon : Int = dataList.size
        dataList.add(orderListData)
        notifyItemInserted(positon)
    }

    // 들어갈 item에 뷰를 붙이는것
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_orderlist,parent,false)
        return Holder(view);
    }


    // inner class : 어차피 이 클래스에서는 쓰는 거이기 때문에..
    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var tv_order_number : TextView =itemView.findViewById(R.id.tv_order_item_odernumber)
        var tv_order_time : TextView = itemView.findViewById(R.id.tv_order_item_order_time)
        var tv_store_name: TextView = itemView.findViewById(R.id.tv_order_item_store_name)
        var iv_flag_state : ImageView=itemView.findViewById(R.id.iv_order_item_order_present)
        var btn_order_detaild: Button = itemView.findViewById(R.id.btn_order_item_detailed)

    }



}