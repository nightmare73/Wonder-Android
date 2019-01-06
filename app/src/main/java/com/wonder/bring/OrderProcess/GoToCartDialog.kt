package com.wonder.bring.OrderProcess

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.wonder.bring.R
import kotlinx.android.synthetic.main.dialog_go_to_cart.*

class GoToCartDialog (ctx: Context): Dialog(ctx) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_go_to_cart)

        btn_go_to_cart_dialog_cart.setOnClickListener {
            // todo : 임시로 확인해본 것 --> 실제로 장바구니 탭으로 가야해
            Log.d("누름", "누름")
        }

        btn_go_to_cart_dialog_close.setOnClickListener {
            dismiss()
        }
    }
}