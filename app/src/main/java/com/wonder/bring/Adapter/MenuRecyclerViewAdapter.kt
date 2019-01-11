package com.wonder.bring.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wonder.bring.OrderProcess.OrderActivity
import com.wonder.bring.R
import com.wonder.bring.data.MenuListData
import org.jetbrains.anko.startActivity

class MenuRecyclerViewAdapter(val ctx: Context, val listDataList: ArrayList<MenuListData>) :
    RecyclerView.Adapter<MenuRecyclerViewAdapter.Holder>() {

    var storeIdx:Int=-1

    override fun onBindViewHolder(holder: Holder, position: Int) {
//        requestManager.load(listDataList[position].photoUrl).into(holder.menu_photo)

        holder.menu_name.text = listDataList[position].name
        holder.menu_price.text = listDataList[position].price.toString() + "원"

        holder.whole_btn.setOnClickListener {
            ctx.startActivity<OrderActivity>(
                "menuIdx" to listDataList[position].menuIdx,
                "storeIdx" to storeIdx,
                "menuName" to listDataList[position].name,
                "photoUrl" to listDataList[position].photoUrl
            )
        }

        val requestOptions = RequestOptions()
        Glide.with(ctx)
            .setDefaultRequestOptions(requestOptions)
            .load(listDataList[position].photoUrl)
            .thumbnail(0.5f)
            .into(holder.menu_photo)

    }

    override fun getItemCount(): Int {
        return listDataList.size
    }

    fun addNewItem(menuListData: MenuListData) {     //추가하는거 여기 adapter쪽에서도 붙여도 된다.
        val positon: Int = listDataList.size
        listDataList.add(menuListData)
        notifyItemInserted(positon)
    }

    // 들어갈 item에 뷰를 붙이는것
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_menu_item, parent, false)
        return Holder(view);
    }


    // inner class : 어차피 이 클래스에서는 쓰는 거이기 때문에..
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val menu_name: TextView = itemView.findViewById(R.id.tv_rv_menu_item_menu_name) as TextView
        val menu_price: TextView = itemView.findViewById(R.id.tv_rv_menu_item_menu_price) as TextView
        val menu_photo: ImageView = itemView.findViewById(R.id.iv_menu_item_menu_photo) as ImageView
        val whole_btn: RelativeLayout = itemView.findViewById(R.id.btn_menu_item_whole_box) as RelativeLayout
    }

}