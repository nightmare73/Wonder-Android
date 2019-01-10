package com.wonder.bring.LoginProcess

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.wonder.bring.R
import kotlinx.android.synthetic.main.activity_agreement.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textColor

class AgreementActivity : AppCompatActivity() {

    var id: String = ""
    var pass: String = ""
    var nick: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement)

        variableInit()
        setCheckBoxListener()

        Log.v("Malibin Debug","id : $id, pass : $pass, nick : $nick")

    }

    private fun variableInit(){

        id = intent.getStringExtra("id")
        pass = intent.getStringExtra("pass")
        nick = intent.getStringExtra("nick")
    }

    private fun setCheckBoxListener() {

        cb_agree_act_whole.setOnClickListener {

            if (cb_agree_act_whole.isChecked) {
                cb_agree_act_one.isChecked = true
                cb_agree_act_two.isChecked = true
            } else {
                cb_agree_act_one.isChecked = false
                cb_agree_act_two.isChecked = false
            }
            completeCheck()
        }

        cb_agree_act_one.setOnClickListener {

            checkWhole()
            completeCheck()
        }

        cb_agree_act_two.setOnClickListener {

            checkWhole()
            completeCheck()
        }
    }

    private fun checkWhole() {
        if (cb_agree_act_one.isChecked && cb_agree_act_two.isChecked)
            cb_agree_act_whole.isChecked = true
        else
            cb_agree_act_whole.isChecked = false
    }

    private fun completeCheck() {
        if (cb_agree_act_whole.isChecked) {
            btn_agree_act_submit.isEnabled = true
            btn_agree_act_submit.backgroundResource = R.color.salmon
            btn_agree_act_submit.textColor = Color.parseColor("#FFFFFF")
        } else {
            btn_agree_act_submit.isEnabled = false
            btn_agree_act_submit.backgroundResource = R.color.white
            btn_agree_act_submit.textColor = Color.parseColor("#909090")
        }
    }


}