package com.wonder.bring.OrderProcess

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import com.wonder.bring.MainProcess.MainActivity
import com.wonder.bring.R
import kotlinx.android.synthetic.main.activity_payment.*
import org.jetbrains.anko.toast

class PayCheckActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val totalPrice = intent.getIntExtra("totalPrice", -1)
        tv_payment_act_order_price.text=totalPrice.toString()+"원"
        tv_payment_act_total_price.text=totalPrice.toString()+"원"


        rl_order_total_act_pay.setOnClickListener {
            toast("결제가 완료되었습니다.").setGravity(Gravity.CENTER_VERTICAL, 0, 0)

            // todo: 결제가 완료되면, 주문내역화면으로
            val intent : Intent= Intent(applicationContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()

        }
    }
}
