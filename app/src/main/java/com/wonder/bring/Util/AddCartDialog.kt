package com.wonder.bring.Util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.wonder.bring.R
import com.wonder.bring.db.CartData
import kotlinx.android.synthetic.main.dialog_bring_type.*
import org.jetbrains.anko.imageResource


class AddCartDialog(private val ctx: Context, val data: CartData, val userId: String) : Dialog(ctx) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_bring_type)
        window.setBackgroundDrawableResource(R.color.transparent)

        viewInit()

        putCartItem(data)

    }

    private fun putCartItem(data: CartData) {

        Cart(ctx).addCartList(userId, data)
    }

    private fun viewInit() {

        iv_bring_dialog_icon.imageResource = R.drawable.big_popup_logo_cart
        tv_bring_dialog_message.text = ("장바구니에\n상품이 담겼습니다.")
        tv_bring_dialog_top.text = "장바구니 가기"
        tv_bring_dialog_bottom.text = "계속 쇼핑"

        btn_bring_dialog_top.setOnClickListener {

        }

        btn_bring_dialog_bottom.setOnClickListener {

            Log.v("Malibin Debug", "AddCartDialog에서 SharedPreference 데이터 확인 : " + Cart(ctx).loadCartDataString(userId))
            dismiss()
        }


    }
}