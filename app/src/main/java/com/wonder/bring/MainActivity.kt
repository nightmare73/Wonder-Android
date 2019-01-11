package com.wonder.bring

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.wonder.bring.MainFragment.MainFragmentViewPager
import com.wonder.bring.MainFragment.MyFragmentStatePagerAdapter
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.NetworkService
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.wonder.bring.LoginProcess.LoginActivity
import com.wonder.bring.db.CartData
import com.wonder.bring.db.SharedPreferenceController
import kotlinx.android.synthetic.main.fragment_login_no.view.*
import org.jetbrains.anko.support.v4.startActivity
import java.lang.reflect.Type


class MainActivity : AppCompatActivity() {


    // 통신이 들어가는 곳은 다 써주자.
    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    // backpressed변수
    var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureBottomNavigation()


        var gson: Gson = GsonBuilder().create()
        var cartdata = CartData(
            100, 200, "유알엘데스네", "가게이름",
            "메뉴이름", "요청사항데스네", 3, 1, 7000
        )
        var cartlist: ArrayList<CartData> = ArrayList()
        cartlist.add(cartdata)
        cartlist.add(cartdata)
        Log.v("Malibin Debug", "cart 객체 json : " + gson.toJson(cartlist))
        var db = SharedPreferenceController
        db.setCartData(applicationContext, "mome", gson.toJson(cartlist))
        Log.v("Malibin Debug", "cart 객체 json 저장됫던거 불러오면 : " + db.getCartData(applicationContext, "mome"))
        var outputCartList: ArrayList<CartData>? = ArrayList()


        var type: Type = object : TypeToken<ArrayList<CartData>>(){}.type
        outputCartList = gson.fromJson(db.getCartData(applicationContext,"mome"),type)
        //outputCartList = gson.fromJson<ArrayList<CartData>>(db.getCartData(applicationContext,"mome"),CartData::class.java)
        //(db.getCartData(applicationContext,"mome"), object : ArrayList<CartData>(){}.javaClass) as? ArrayList<CartData>
        Log.v("Malibin Debug","꺼낸걸 바꾼거:"+outputCartList.toString())
        Log.v("Malibin Debug",outputCartList!![0].storeName+"  "+outputCartList[1].cost)




    }

    // 탭바 추가 함수
    private fun configureBottomNavigation() {

        // 뷰페이저 스와이프 막기 위해서, customviewpager사용
        val customViewPager = findViewById(R.id.vp_bottom_main_act_frag_pager) as MainFragmentViewPager
        customViewPager.setPagingEnabled(false)
        customViewPager.adapter = MyFragmentStatePagerAdapter(supportFragmentManager, 4)
        customViewPager.offscreenPageLimit = 4

        tl_bottom_main_act_bottom_menu.setupWithViewPager(vp_bottom_main_act_frag_pager)

        val bottomNaviLayout: View = this.layoutInflater.inflate(R.layout.bottom_navigation_tab, null, false)


            tl_bottom_main_act_bottom_menu.getTabAt(0)!!.customView =
                    bottomNaviLayout.findViewById(R.id.btn_bottom_navi_main_tab) as RelativeLayout
            tl_bottom_main_act_bottom_menu.getTabAt(1)!!.customView =
                    bottomNaviLayout.findViewById(R.id.btn_bottom_navi_orderlist_tab) as RelativeLayout
            tl_bottom_main_act_bottom_menu.getTabAt(2)!!.customView =
                    bottomNaviLayout.findViewById(R.id.btn_bottom_navi_cart_tab) as RelativeLayout
            tl_bottom_main_act_bottom_menu.getTabAt(3)!!.customView =
                    bottomNaviLayout.findViewById(R.id.btn_bottom_navi_my_page_tab) as RelativeLayout


    }

    // 해당 탭으로 이동
     public fun moveToTab(position: Int) {
        vp_bottom_main_act_frag_pager.setCurrentItem(position)
    }

    override fun onBackPressed() {
        var temp: Long = System.currentTimeMillis()
        var intervalTime: Long = temp - backPressedTime
        if (intervalTime in 0..2000) {
            super.onBackPressed()
        } else {
            backPressedTime = temp
            toast("버튼을 한번 더 누르면 종료됩니다.")
        }
    }




}






