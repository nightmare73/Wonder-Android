package com.wonder.bring.OrderProcess

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.wonder.bring.BringTypeDialog
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.Get.GetMenuDetailsResponseData
import com.wonder.bring.Network.Get.OtherDataClasses.MenuSize
import com.wonder.bring.Network.NetworkService
import com.wonder.bring.R
import com.wonder.bring.StoreProcess.StoreActivity
import kotlinx.android.synthetic.main.activity_order.*
import org.jetbrains.anko.toast
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
    var menusize=-1;
    var menuPrice=0;
    var menuSize: ArrayList<MenuSize> = ArrayList()
    lateinit var requestManager : RequestManager


    private val TAG = OrderActivity::class.java!!.getSimpleName()

    // 보미 서버 통신
    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        requestManager = Glide.with(this)
        variableInit()
        viewInit()
        setOnBtnClickListner()
        menusizeSpinner()

    }

    private fun variableInit() {
        //-1이 안넘어가게 꼭 예외처리 해야함 -1이 그대로 서버로 넘어가면 터짐
        storeIdx = intent.getIntExtra("storeIdx", -1)
        Log.d(TAG,storeIdx.toString())
        menuIdx = intent.getIntExtra("menuIdx", -1)
        Log.d(TAG,menuIdx.toString())
        photoUrl = intent.getStringExtra("photoUrl")
        requestManager.load(photoUrl).into(iv_order_act_menu_image)
        menuName = intent.getStringExtra("menuName")
        tv_order_act_menu_name.text = menuName

        networkService.getMenuDetailsRequest("application/json", storeIdx, menuIdx).enqueue(object :
            Callback<GetMenuDetailsResponseData> {
            override fun onFailure(call: Call<GetMenuDetailsResponseData>, t: Throwable) {
                Log.d(TAG,"서버실패")
            }

            override fun onResponse(call: Call<GetMenuDetailsResponseData>, response: Response<GetMenuDetailsResponseData>
            ) {

                var status = response.body()!!.status



                when (status) {
                    200 -> {
                        menuSize = response.body()!!.data
                        Log.d(TAG,menuSize.toString())



                        //4일경우,
                        when(menuSize.get(0).size) {


                            //case 1) 디저트일경우
                            4 -> {
                                //1. spinner 막아줘야 하고
                                //2. 위에 가격 4디저트 가격으로 적어주고
                                //3. 총 주문 금액도 4디저트 가격으로?
                                menusize = 4
                                menuPrice = menuSize.get(0).price


                                tv_order_act_menu_price.text = menuPrice.toString()


                                totalPrice = menuPrice * quantity           // 총 주문 금액 넣기 위해서
                                tv_order_act_total_price.text = totalPrice.toString() + "원"

                                // 메뉴 스피너 비지블


                            }

                            //case 2) 옵션일경우
                            5 -> {
                                menusize = 5
                                menuPrice = menuSize.get(0).price


                                tv_order_act_menu_price.text = menuPrice.toString()


                                totalPrice = menuPrice * quantity           // 총 주문 금액 넣기 위해서
                                tv_order_act_total_price.text = totalPrice.toString() + "원"
                            }

                            // case 3) 음료일경우
                            in 0..2 -> {

                                // 뭐가 올지 모르기 때문에
                                for (i in 0..menuSize.size - 1) {

                                    Log.d(TAG, menuSize.get(i).size.toString())
                                    Log.d(TAG, menuSize.get(i).price.toString())

                                    menusize = menuSize.get(i).size
                                    menuPrice = menuSize.get(i).price


                                    // 만약 regular가 있다면
                                    if (menuSize.get(i).size == 1) {
                                        tv_order_act_menu_price.text = menuPrice.toString()
                                    }


                                    totalPrice = menuPrice * quantity           // 총 주문 금액 넣기 위해서
                                    tv_order_act_total_price.text = totalPrice.toString() + "원"

                                }


                            }


                        }


//                        menuSize.get(0)
                    }
                    404 -> {
                        //메뉴와 매장은 존재하지만, 해당 menu가 해당 store에 없는 경우에도 이렇게 뜸
                        //메뉴에 사이즈와 가격 정보가 없을 때
                        //뭘 넣어줘야하지??
                        if(response.body()!!.message.equals("메뉴 상세 정보를 찾을 수 없습니다."))
                            Log.d(TAG,"메뉴 상세 정보를 찾을 수 없습니다")
                        else if(response.body()!!.message.equals("사이즈와 가격 정보가 없습니다."))
                            Log.d(TAG,"사이즈와 가격 정보가 없습니다")
                    }
                }
            }
        })

        Log.v("Malibin Debug", "storeIdx : $storeIdx, menuIdx : $menuIdx, menuName : $menuName, photoUrl : $photoUrl")
    }

    private fun viewInit() {


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

            quantity=quantity--
            tv_order_act_menu_quantity.text=quantity.toString()
        }

        //plus 버튼
        btn_order_act_plus_icon.setOnClickListener {

            // 수량 하나씩 증가
            quantity=quantity++
            tv_order_act_menu_quantity.text=quantity.toString()
        }


    }

    // 메뉴 스피너
    fun menusizeSpinner() {

        val spnMenuSize = findViewById<View>(R.id.spinner_order_act_size) as Spinner
        val adpter = ArrayAdapter.createFromResource(this, R.array.spinner_menu_size, R.layout.item_spinner_menu_size)
        adpter.setDropDownViewResource(R.layout.item_spinner_menu_size_dropdown)
        spnMenuSize.setAdapter(adpter)
        spnMenuSize.setSelection(1)  // regular 디폴트로 주기


        spnMenuSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {


            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selItem = spnMenuSize.getSelectedItem() as String


                if(position==0){

                }




            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }
}
