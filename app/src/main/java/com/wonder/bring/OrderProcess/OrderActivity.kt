package com.wonder.bring.OrderProcess

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.wonder.bring.MainProcess.MainActivity
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.Get.GetMenuDetailsResponseData
import com.wonder.bring.Network.Get.OtherDataClasses.MenuSize
import com.wonder.bring.Network.Get.OtherDataClasses.OrderList
import com.wonder.bring.Network.NetworkService
import com.wonder.bring.Network.Post.PostOrderRequest
import com.wonder.bring.Network.Post.PostOrderResponse
import com.wonder.bring.R
import com.wonder.bring.Util.AddCartDialog
import com.wonder.bring.Util.MFlags
import com.wonder.bring.Util.SizeConvertor
import com.wonder.bring.Network.Get.OtherDataClasses.CartData
import com.wonder.bring.Util.SharedPreferenceController
import kotlinx.android.synthetic.main.activity_order.*
import org.jetbrains.anko.startActivity
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
    var storeName: String = ""
    var menuIdx: Int = -1
    var photoUrl: String = ""
    var menuName: String = ""
    var menusize = -1;
    var menuPrice = 0;
    var menuSize: ArrayList<MenuSize> = ArrayList()
    var selItem: String = ""

    lateinit var requestManager: RequestManager
    var OrderMenuListdata: ArrayList<OrderList> = ArrayList()

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

    }

    private fun variableInit() {
        //-1이 안넘어가게 꼭 예외처리 해야함 -1이 그대로 서버로 넘어가면 터짐
        storeIdx = intent.getIntExtra("storeIdx", -1)
        storeName = intent.getStringExtra("storeName")
        menuIdx = intent.getIntExtra("menuIdx", -1)
        photoUrl = intent.getStringExtra("photoUrl")
        menuName = intent.getStringExtra("menuName")

        requestManager.load(photoUrl).into(iv_order_act_menu_image)

        tv_order_act_menu_name.text = menuName

        networkService.getMenuDetailsRequest("application/json", storeIdx, menuIdx).enqueue(object :
            Callback<GetMenuDetailsResponseData> {
            override fun onFailure(call: Call<GetMenuDetailsResponseData>, t: Throwable) {
                Log.d(TAG, "서버실패")
            }

            override fun onResponse(
                call: Call<GetMenuDetailsResponseData>, response: Response<GetMenuDetailsResponseData>
            ) {

                var status = response.body()!!.status


                when (status) {
                    200 -> {
                        menuSize = response.body()!!.data
                        Log.d(TAG, menuSize.toString())


                        when (menuSize.size) {
                            // 1개 일때 : 4이거나 5인경우만 해당
                            1 -> {
                                // regular인경우..
                                if (menuSize.get(0).size == 1) {
                                    menusize = 1
                                    menusizeSpinner(1, 1)
                                } else {
                                    menusize = menuSize.get(0).size
                                    menuPrice = menuSize.get(0).price // 4일때는 무조건 0번째일테니까
                                    tv_order_act_menu_price.text = menuPrice.toString() // 가격 그대로 적어주어야 하고

                                    totalPrice = menuPrice * quantity                // 총 주문 금액 넣기 위해서
                                    tv_order_act_total_price.text = totalPrice.toString() + "원"

                                    // spinner 아예 막아주자
                                    menusizeSpinner(1, 0)
                                }


                            }

                            // 2일때 : 1,2 일때 / 0,1일때
                            2 -> {
                                //regular인 경우

                                //서버한테 오는 첫번째가 [regular,large]
                                if (menuSize.get(0).size == 1) {
                                    menuPrice = menuSize.get(0).price
                                    tv_order_act_menu_price.text = menuPrice.toString()

                                    //서버한테 오는 첫번째가 [small, regular]
                                } else {
                                    menuPrice = menuSize.get(1).price
                                    tv_order_act_menu_price.text = menuPrice.toString()
                                }


                                totalPrice = menuPrice * quantity
                                tv_order_act_total_price.text = totalPrice.toString() + "원"


                                menusizeSpinner(2, 1)
                            }
                            //3일때
                            3 -> {
                                // spinner 3개 기본 생성
                                // regular일때

                                // 위에 regular 적고
                                menuPrice = menuSize.get(1).price
                                tv_order_act_menu_price.text = menuPrice.toString()

                                // 총 주문 금액 넣기 위해서
                                totalPrice = menuPrice * quantity
                                tv_order_act_total_price.text = totalPrice.toString() + "원"


                                menusizeSpinner(3, 1)
                            }

                        }


                    }

                    404 -> {
                        //메뉴와 매장은 존재하지만, 해당 menu가 해당 store에 없는 경우에도 이렇게 뜸
                        //메뉴에 사이즈와 가격 정보가 없을 때
                        //뭘 넣어줘야하지??
                        if (response.body()!!.message.equals("메뉴 상세 정보를 찾을 수 없습니다."))
                            Log.d(TAG, "메뉴 상세 정보를 찾을 수 없습니다")
                        else if (response.body()!!.message.equals("사이즈와 가격 정보가 없습니다."))
                            Log.d(TAG, "사이즈와 가격 정보가 없습니다")
                    }
                }
            }
        })

        Log.v("Malibin Debug", "storeIdx : $storeIdx, menuIdx : $menuIdx, menuName : $menuName, photoUrl : $photoUrl")
    }

    private fun viewInit() {


    }

    private fun setOnBtnClickListner() {

        btn_order_act_back.setOnClickListener {
            finish()
        }

        //위의 장바구니 아이콘
        btn_order_act_gocart.setOnClickListener {

            var intent = Intent(this, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            setResult(MFlags.RESULT_TO_CART, intent)

            finish()
            startActivity(intent)

        }

        // 장바구니 담기
        btn_order_act_move_to_cart.setOnClickListener {

            val request = et_order_act_request.text.toString()

            val moveToCartDialog =
                AddCartDialog(
                    this,
                    CartData(
                        storeIdx, menuIdx, photoUrl, storeName, menuName, request, quantity, menusize, totalPrice
                    ),
                    SharedPreferenceController.getId(applicationContext)

                )
            moveToCartDialog.show()
        }

        // 바로 주문하기
        btn_order_act_move_to_order.setOnClickListener {
            //통신
            postOrderRequest()
        }

        // minus 버튼
        btn_order_act_minor_icon.setOnClickListener {
            //             toast(tv_order_act_menu_quantity.text.toString())


            if (quantity > 0) {
                quantity = quantity - 1 // 0밑으로 가는것을 막기 위해서
                tv_order_act_menu_quantity.text = quantity.toString()

                totalPrice = menuPrice * quantity           // 총 주문 금액 넣기 위해서
                tv_order_act_total_price.text = totalPrice.toString() + "원"
            }

        }

        //plus 버튼
        btn_order_act_plus_icon.setOnClickListener {

            //todo: 수량 제한을?
            // 수량 하나씩 증가
            quantity = quantity + 1
            tv_order_act_menu_quantity.text = quantity.toString()

            totalPrice = menuPrice * quantity           // 총 주문 금액 넣기 위해서
            tv_order_act_total_price.text = totalPrice.toString() + "원"
        }


    }

    fun postOrderRequest() {
        var memo = et_order_act_request.text.toString()
        var token = SharedPreferenceController.getAuthorization(applicationContext)
//        var tmp_token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJEb0lUU09QVCIsInVzZXJfaWR4IjoxfQ.xmbvRqaMuYnGvtPaV_Lw7HorI5blZHlpT7WQgo5ybvM"

        OrderMenuListdata.add(OrderList(menuIdx, menusize, quantity, memo, totalPrice))
        var body = PostOrderRequest(storeIdx, OrderMenuListdata)


        var postOrderResponse = networkService.postOrderResponse(token, body)

        Log.d(TAG, postOrderResponse.toString())
        postOrderResponse.enqueue(object : Callback<PostOrderResponse> {
            override fun onFailure(call: Call<PostOrderResponse>?, t: Throwable?) {
                Toast.makeText(applicationContext, "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<PostOrderResponse>?, response: Response<PostOrderResponse>?) {
                if (response!!.isSuccessful) {
                    if (response.body()!!.status == 201) {      //1. 주문하기 성공
                        var message = response.body()
                        Log.d(TAG, message.toString())

                        // paymentActivity 로 이동
                        startActivity<PayCheckActivity>("totalPrice" to totalPrice)


                    } else if (response.body()!!.status == 400) {        // 주문하기 실패
                        var message = response.body()
                        Log.d(TAG, message.toString())
                    } else if (response.body()!!.status == 401) {            // 인증실패
                        var message = response.body()
                        Log.d(TAG, message.toString())
                    } else if (response.body()!!.status == 600) {
                        var message = response.body()
                        Log.d(TAG, message.toString())
                    }


                } else Log.d(TAG, "OrderActivity 통신 실패 ")
            }
        })
    }

    // 메뉴 스피너
    fun menusizeSpinner(size: Int, flag: Int) {

        var spnMenuSize = findViewById<View>(R.id.spinner_order_act_size) as Spinner


        when (size) {
            1 -> {// 디저트나 옵션일경우
                if (flag == 0) {
                    spinner_order_act_size.isEnabled = false
                } else if (flag == 1) {
                    val menu_list = arrayOf("regular")

                    val adapter = ArrayAdapter(applicationContext, R.layout.item_spinner_menu_size, menu_list)
                    adapter.setDropDownViewResource(R.layout.item_spinner_menu_size_dropdown)
                    spnMenuSize.setAdapter(adapter)
                    spnMenuSize.setSelection(0)

                    spnMenuSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                            menuPrice = menuSize.get(0).price
                            tv_order_act_menu_price.text = menuPrice.toString()

                            totalPrice = menuPrice * quantity
                            tv_order_act_total_price.text = totalPrice.toString() + "원"

                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {

                        }
                    }

                }

            }

            2 -> {
                val menu_list = arrayOf("regular", "large")

                val adapter = ArrayAdapter(applicationContext, R.layout.item_spinner_menu_size, menu_list)
                adapter.setDropDownViewResource(R.layout.item_spinner_menu_size_dropdown)
                spnMenuSize.setAdapter(adapter)
                spnMenuSize.setSelection(0)

                spnMenuSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                        when (position) {
                            0 -> {// regular

                                menusize = SizeConvertor.parseSizeInt("regular")
                                menuPrice = menuSize.get(0).price


                                totalPrice = menuPrice * quantity
                                tv_order_act_total_price.text = totalPrice.toString() + "원"

                            }
                            1 -> {//large
                                menusize = SizeConvertor.parseSizeInt("large")
                                menuPrice = menuSize.get(1).price


                                totalPrice = menuPrice * quantity
                                tv_order_act_total_price.text = totalPrice.toString() + "원"

                            }
                        }


                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }


            }

            3 -> {
                var adapter =
                    ArrayAdapter.createFromResource(this, R.array.spinner_menu_size, R.layout.item_spinner_menu_size)

                adapter.setDropDownViewResource(R.layout.item_spinner_menu_size_dropdown)
                spnMenuSize.setAdapter(adapter)
                spnMenuSize.setSelection(1)  // regular 디폴트로 주기

                spnMenuSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {


                        // 누를때마다 가격변동이 일어나게끔~
                        when (position) {
                            0 -> {// small
                                menusize = SizeConvertor.parseSizeInt("small")
                                menuPrice = menuSize.get(0).price


                                totalPrice = menuPrice * quantity
                                tv_order_act_total_price.text = totalPrice.toString() + "원"

                            }
                            1 -> {//regular
                                menusize = SizeConvertor.parseSizeInt("regular")
                                menuPrice = menuSize.get(1).price


                                totalPrice = menuPrice * quantity
                                tv_order_act_total_price.text = totalPrice.toString() + "원"
                            }
                            2 -> {//large
                                menusize = SizeConvertor.parseSizeInt("large")
                                menuPrice = menuSize.get(2).price


                                totalPrice = menuPrice * quantity
                                tv_order_act_total_price.text = totalPrice.toString() + "원"
                            }
                        }

                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }


            }


        }


    }


}
