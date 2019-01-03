package com.wonder.bring.OrderProcess

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.wonder.bring.GoToCartDialog
import com.wonder.bring.R
import kotlinx.android.synthetic.main.activity_order.*
import org.jetbrains.anko.toast

class OrderActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        setOnBtnClickListner()
        menusizeSpinner()

}

    private fun setOnBtnClickListner(){

        // 장바구니로 이동
        btn_order_act_move_to_cart.setOnClickListener {
            toast("장바구니로 이동")


            val moveToCartDialog : GoToCartDialog = GoToCartDialog(this)
            moveToCartDialog.show()

        }

        // 바로 주문하기
        btn_order_act_move_to_order.setOnClickListener {

        }


    }

    // 메뉴 스피너
    fun menusizeSpinner(){

        val spnMenuSize=findViewById<View>(R.id.spinner_order_act_size) as Spinner
        val adpter=ArrayAdapter.createFromResource(this,R.array.spinner_menu_size,R.layout.item_spinner_menu_size)
        adpter.setDropDownViewResource(R.layout.item_spinner_menu_size_dropdown)
        spnMenuSize.setAdapter(adpter)


        spnMenuSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selItem = spnMenuSize.getSelectedItem() as String
                Log.d("item",selItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }
}
