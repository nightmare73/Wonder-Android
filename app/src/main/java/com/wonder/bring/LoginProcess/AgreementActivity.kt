package com.wonder.bring.LoginProcess

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.wonder.bring.R
import kotlinx.android.synthetic.main.activity_agreement.*
import org.jetbrains.anko.startActivity

class AgreementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement)

        setOnBtnClickListner()

    }

    // all 체크 할때만  완료버튼 색깔 변하게 하기  1.전체  2. 2번째  3. 3번째 4. 2,3번째누르면 1번째에 불들어오고, 1번째에 불이 드러오니까 완료버튼에도 불이 들어와야함.
//    et_agreement_act_pw.addTextChangedListener(object : TextWatcher{
//
//        override fun afterTextChanged(p0: Editable?) {
//            input_id = et_login_act_id.text.toString()
//            input_pw = et_login_act_pw.text.toString()
//
//            if(input_id.length > 0 && input_pw.length > 0)
//                btn_agreement_act_login.isSelected=true
//            else
//                btn_login_act_login.isSelected=false
//
//        }
//
//        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//        }
//
//        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//        }
//    })


//    동의체크안하면 비활성화시켜야하는데 ㅠㅠㅠ
    private fun setOnBtnClickListner() {
        btn_agreemnet_act_finish.setOnClickListener {
            startActivity<LoginActivity>()
        }

    }
}