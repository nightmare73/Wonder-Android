package com.wonder.bring.OrderProcess

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.bumptech.glide.Glide
import com.wonder.bring.BringTypeDialog
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.Get.GetMenuDetailsResponseData
import com.wonder.bring.Network.Get.OtherDataClasses.MenuSize
import com.wonder.bring.Network.NetworkService
import com.wonder.bring.R
import kotlinx.android.synthetic.main.activity_order.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderActivity : AppCompatActivity() {

    var quantity: Int = 1
    var totalPrice: Int = 0

    //이게 그대로 -1 넘어가면 안된다!!!!
    //이 변수들은 이전 액티비티와 프래그먼트들로 부터 꾸역꾸역 가지고온 데이터들임.
    //이 변수는 따로 variableInit()에서 초기화.
    var storeIdx: Int = -1
    var menuIdx: Int = -1
    var photoUrl: String = ""
    var menuName: String = ""
    var menuSize: ArrayList<MenuSize> = ArrayList()

    // 보미 서버 통신
    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        variableInit()
        viewInit()
        setOnBtnClickListner()
        menusizeSpinner()

    }

    private fun variableInit() {
        //-1이 안넘어가게 꼭 예외처리 해야함 -1이 그대로 서버로 넘어가면 터짐
        storeIdx = intent.getIntExtra("storeIdx", -1)
        menuIdx = intent.getIntExtra("menuIdx", -1)
        photoUrl = intent.getStringExtra("photoUrl")
        menuName = intent.getStringExtra("menuName")

        networkService.getMenuDetailsRequest("application/json", storeIdx, menuIdx).enqueue(object :
            Callback<GetMenuDetailsResponseData> {
            override fun onFailure(call: Call<GetMenuDetailsResponseData>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<GetMenuDetailsResponseData>,
                response: Response<GetMenuDetailsResponseData>
            ) {
                var status = response.body()!!.status

                when (status) {
                    200 -> menuSize = response.body()!!.data.sizePrices
                    404 -> {
                        //메뉴와 매장은 존재하지만, 해당 menu가 해당 store에 없는 경우에도 이렇게 뜸
                        //메뉴에 사이즈와 가격 정보가 없을 때
                        //뭘 넣어줘야하지??
                    }
                }
            }
        })
        Log.v("Malibin Debug", "storeIdx : $storeIdx, menuIdx : $menuIdx, menuName : $menuName, photoUrl : $photoUrl")
    }

    private fun viewInit() {
        Glide.with(this).load(photoUrl).into(iv_order_act_menu_image)
        tv_order_act_menu_name.text = menuName

    }

    private fun setOnBtnClickListner() {

        // 장바구니로 이동
        btn_order_act_move_to_cart.setOnClickListener {
            val moveToCartDialog = BringTypeDialog(this, BringTypeDialog.CART_TYPE)
            moveToCartDialog.show()
        }

        // 바로 주문하기
        btn_order_act_move_to_order.setOnClickListener {

        }

        // minus 버튼
        btn_order_act_minor_icon.setOnClickListener {
            //             toast(tv_order_act_menu_quantity.text.toString())

        }

        //plus 버튼
        btn_order_act_plus_icon.setOnClickListener {
            //            val menu_quantity=Integer.parseInt(tv_order_act_menu_quantity.toString())

        }


    }

    // 메뉴 스피너
    fun menusizeSpinner() {

        val spnMenuSize = findViewById<View>(R.id.spinner_order_act_size) as Spinner
        val adpter = ArrayAdapter.createFromResource(this, R.array.spinner_menu_size, R.layout.item_spinner_menu_size)
        adpter.setDropDownViewResource(R.layout.item_spinner_menu_size_dropdown)
        spnMenuSize.setAdapter(adpter)

        spnMenuSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selItem = spnMenuSize.getSelectedItem() as String
                Log.d("item", selItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }
}
