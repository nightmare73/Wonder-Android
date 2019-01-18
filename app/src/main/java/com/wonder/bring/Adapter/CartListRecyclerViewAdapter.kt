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
import com.airbnb.lottie.parser.IntegerParser
import com.bumptech.glide.Glide
import com.wonder.bring.MainActivity
import com.wonder.bring.MainFragment.CartFragment
import com.wonder.bring.R
import com.wonder.bring.Util.Cart
import com.wonder.bring.Util.SizeConvertor
import com.wonder.bring.db.CartData
import com.wonder.bring.db.SharedPreferenceController
import org.jetbrains.anko.db.INTEGER
import java.lang.IndexOutOfBoundsException


class CartListRecyclerViewAdapter(val ctx: Context, val dataList: ArrayList<CartData>) :
    RecyclerView.Adapter<CartListRecyclerViewAdapter.Holder>() {

    private val userId = SharedPreferenceController.getId(ctx)
    private val cart = Cart(ctx)

    override fun onBindViewHolder(holder: Holder, position: Int) {

        //아 이런 캐스팅 개오반데
        (((ctx as MainActivity).pa.getItem(2)) as CartFragment).refreshTotalPrice(getTotalPrice())

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

            var tempCart = cart.loadCartData(userId)

            Log.v("Malibin Debug", "장바구니 삭제직전 불러온 카트데이터 : " + tempCart.toString())

            tempCart.removeAt(position)

            cart.saveCartData(userId, tempCart)

            Log.v("Malibin Debug", "장바구니 삭제직후 불러온 카트데이터 : " + cart.loadCartDataString(userId))

            //아 이런 캐스팅 개오반데
            (((ctx as MainActivity).pa.getItem(2)) as CartFragment).refreshTotalPrice(getTotalPrice())
        }

        holder.btn_plus.setOnClickListener {

            var quantity: Int = dataList[position].quantity
            var originalCost: Int = dataList[position].cost / quantity
            var tempCart = cart.loadCartData(userId)

            Log.v("Malibin Debug", "장바구니 수량 수정 직전 불러온 카트데이터 : " + tempCart.toString())

            tempCart[position].quantity = quantity + 1
            tempCart[position].cost = (originalCost * (quantity + 1))

            //그냥 밑에잇는걸 저장하면 되는거아님??? 혼자잇을때 더 생각해보자.
            dataList[position].quantity = quantity + 1
            dataList[position].cost = (originalCost * (quantity + 1))

            cart.saveCartData(userId, tempCart)

            notifyItemChanged(position)

            Log.v("Malibin Debug", "장바구니 수량 수정 직후 불러온 카트데이터 : " + cart.loadCartDataString(userId))

            //아 이런 캐스팅 개오반데
            (((ctx as MainActivity).pa.getItem(2)) as CartFragment).refreshTotalPrice(getTotalPrice())
        }

        holder.btn_minus.setOnClickListener {

            var quantity: Int = dataList[position].quantity
            if (quantity == 1) return@setOnClickListener

            var originalCost: Int = dataList[position].cost / quantity
            var tempCart = cart.loadCartData(userId)

            Log.v("Malibin Debug", "장바구니 수량 수정 직전 불러온 카트데이터 : " + tempCart.toString())

            tempCart[position].quantity = quantity - 1
            tempCart[position].cost = (originalCost * (quantity - 1))

            dataList[position].quantity = quantity - 1
            dataList[position].cost = (originalCost * (quantity - 1))

            cart.saveCartData(userId, tempCart)

            notifyItemChanged(position)

            Log.v("Malibin Debug", "장바구니 수량 수정 직후 불러온 카트데이터 : " + cart.loadCartDataString(userId))

            //아 이런 캐스팅 개오반데
            (((ctx as MainActivity).pa.getItem(2)) as CartFragment).refreshTotalPrice(getTotalPrice())
        }

    }

    override fun getItemCount(): Int = dataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_cart, parent, false)
        return Holder(view)
    }

    fun getTotalPrice(): Int {

        var totalPrice = 0

        for (cost in dataList)
            totalPrice += cost.cost

        return totalPrice
    }

    fun itemRangeInsert() {

        Log.v("Malibin Debug","실행이되긴하니?")

        val userId = SharedPreferenceController.getId(ctx)

        var currentCartItem = Cart(ctx).loadCartData(userId)

        val previousItemCount = itemCount
        val currentItemCount = currentCartItem.size

        if (currentItemCount - previousItemCount <= 0) return

        for (i in 1..previousItemCount)
            currentCartItem.removeAt(0)

        for (data in currentCartItem) {
            dataList.add(data)
        }
        notifyItemRangeInserted(previousItemCount, itemCount)

    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_store_name: TextView = itemView.findViewById(R.id.tv_cart_item_store_name)
        var tv_menu_name: TextView = itemView.findViewById(R.id.tv_cart_item_menu_name)
        var tv_menu_size: TextView = itemView.findViewById(R.id.tv_cart_item_menu_size)
        var tv_menu_price: TextView = itemView.findViewById(R.id.tv_cart_item_menu_price)
        var tv_menu_quantity: TextView = itemView.findViewById(R.id.tv_cart_item_menu_quantity)
        var tv_menu_request: TextView = itemView.findViewById(R.id.tv_cart_item_menu_request)
        var iv_image: ImageView = itemView.findViewById(R.id.iv_cart_item_menu_image)

        var btn_delete: Button = itemView.findViewById(R.id.btn_cart_item_delete)
        var btn_plus: ImageView = itemView.findViewById(R.id.btn_cart_item_plus_icon)
        var btn_minus: ImageView = itemView.findViewById(R.id.btn_cart_item_minor_icon)
    }


}