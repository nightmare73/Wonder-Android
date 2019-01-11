package com.wonder.bring

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import kotlinx.android.synthetic.main.dialog_bring_type.*
import org.jetbrains.anko.imageResource

class BringTypeDialog(ctx: Context, val type: Int): Dialog(ctx){

    companion object {
        const val LOGIN_TYPE: Int = 0
        const val CART_TYPE: Int = 1
        const val WARNING_TYPE: Int = 2
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_bring_type)
        window.setBackgroundDrawableResource(R.color.transparent)

        viewInit(type)
    }


    private fun viewInit(type: Int){

        when(type){

            LOGIN_TYPE -> {
                iv_bring_dialog_icon.imageResource = R.drawable.big_popup_logo_login
                tv_bring_dialog_message.text = ("로그인이\n필요한 기능입니다.")
                tv_bring_dialog_top.text="로그인하러가기"
                tv_bring_dialog_bottom.text = "닫기"

                btn_bring_dialog_top.setOnClickListener {

                }

                btn_bring_dialog_bottom.setOnClickListener {
                    dismiss()
                }
            }

            CART_TYPE -> {
                iv_bring_dialog_icon.imageResource = R.drawable.big_popup_logo_cart
                tv_bring_dialog_message.text = ("장바구니에\n상품이 담겼습니다.")
                tv_bring_dialog_top.text = "장바구니 가기"
                tv_bring_dialog_bottom.text = "계속 쇼핑"

                btn_bring_dialog_top.setOnClickListener {

                }

                btn_bring_dialog_bottom.setOnClickListener {
                    dismiss()
                }
            }

            WARNING_TYPE -> {
                iv_bring_dialog_icon.imageResource = R.drawable.big_popup_logo_warning
                tv_bring_dialog_message.text = ("이 화면을 나가시면\n주문이 취소됩니다.\n그래도 나가시겠습니까?")
                tv_bring_dialog_top.text="예"
                tv_bring_dialog_bottom.text = "아니요"

                btn_bring_dialog_top.setOnClickListener {
                    //액티비티끄는방법?
                }

                btn_bring_dialog_bottom.setOnClickListener {
                    dismiss()
                }
            }
        }
    }

}