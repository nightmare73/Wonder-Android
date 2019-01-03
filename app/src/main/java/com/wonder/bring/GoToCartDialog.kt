package com.wonder.bring

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.dialog_go_to_cart.*

class GoToCartDialog (ctx: Context): AlertDialog(ctx) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_go_to_cart)

//        btn_go_to_cart_dialog_cart.setOnClickListener {
//            Log.d("누름", "누르")
//        }
//
//        btn_go_to_cart_dialog_close.setOnClickListener {
//            dismiss()
//        }
    }
}