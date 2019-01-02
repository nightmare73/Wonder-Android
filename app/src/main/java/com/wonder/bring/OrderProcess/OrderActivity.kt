package com.wonder.bring.OrderProcess

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.wonder.bring.R

class OrderActivity : AppCompatActivity() {

    internal var MenuSize = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        val menuSizeSpinner = findViewById<View>(R.id.spinner_order_act_size) as Spinner
        val menusize_list = arrayOf("REGULAR", "LARGE", "SMALL")

        // 목적 에 대한 Spinner
        val adapter = ArrayAdapter(applicationContext, R.layout.spin,menusize_list)
        adapter.setDropDownViewResource(R.layout.spin_dropdown)

        menuSizeSpinner.adapter = adapter

        menuSizeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                if (position == 0) {
                    MenuSize= "REGULAR"
                } else if (position == 1) {
                    MenuSize= "LARGE"
                } else if (position == 2) {
                    MenuSize = "SMALL"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
}
}
