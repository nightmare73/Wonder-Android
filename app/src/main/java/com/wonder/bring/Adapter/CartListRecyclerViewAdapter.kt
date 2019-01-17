package com.wonder.bring.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.wonder.bring.R
import com.wonder.bring.Util.Cart
import com.wonder.bring.Util.SizeConvertor
import com.wonder.bring.db.CartData
import com.wonder.bring.db.SharedPreferenceController
import java.lang.IndexOutOfBoundsException


class CartListRecyclerViewAdapter(val ctx: Context, val dataList: ArrayList<CartData>) :
    RecyclerView.Adapter<CartListRecyclerViewAdapter.Holder>() {

    var totalPrice: Int = 0


    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tv_store_name.text = dataList[position].storeName
        holder.tv_menu_name.text = dataList[position].menuName
        holder.tv_menu_price.text = (dataList[position].cost.toString() + "원")
        holder.tv_menu_quantity.text = dataList[position].quantity.toString()
        holder.tv_menu_size.text = SizeConvertor.parseSizeString(dataList[position].size)
        holder.tv_menu_request.text = dataList[position].request

        try {
            Glide.with(ctx).load(dataList[position].imageUrl).into(holder.iv_image)
        } catch (e: Exception) {

        }

        holder.btn_delete.setOnClickListener {

            dataList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, dataList.size)

            val userid = SharedPreferenceController.getId(ctx)
            var tempCart = Cart(ctx).loadCartData(userid)

            Log.v("Malibin Debug", "장바구니 삭제직전 불러온 카트데이터 : " + tempCart.toString())

            tempCart.removeAt(position)

            Cart(ctx).saveCartData(userid, tempCart)

            Log.v("Malibin Debug", "장바구니 삭제직후 불러온 카트데이터 : " + Cart(ctx).loadCartDataString(userid))
        }

    }

    override fun getItemCount(): Int = dataList.size


    // 들어갈 item에 뷰를 붙이는것
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_cart, parent, false)
        return Holder(view)
    }


    // inner class : 어차피 이 클래스에서는 쓰는 거이기 때문에..
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_store_name: TextView = itemView.findViewById(R.id.tv_cart_item_store_name)
        var tv_menu_name: TextView = itemView.findViewById(R.id.tv_cart_item_menu_name)
        var tv_menu_size: TextView = itemView.findViewById(R.id.tv_cart_item_menu_size)
        var tv_menu_price: TextView = itemView.findViewById(R.id.tv_cart_item_menu_price)
        var tv_menu_quantity: TextView = itemView.findViewById(R.id.tv_cart_item_menu_quantity)
        var tv_menu_request: TextView = itemView.findViewById(R.id.tv_cart_item_menu_request)

        var iv_image: ImageView = itemView.findViewById(R.id.iv_cart_item_menu_image)

        var btn_delete: Button = itemView.findViewById(R.id.btn_cart_item_delete)
    }


}