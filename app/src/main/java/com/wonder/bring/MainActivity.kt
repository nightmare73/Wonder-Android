package com.wonder.bring

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.google.firebase.messaging.FirebaseMessaging
import com.wonder.bring.MainFragment.MainFragmentViewPager
import com.wonder.bring.MainFragment.MyFragmentStatePagerAdapter
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.NetworkService
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {


    // 통신이 들어가는 곳은 다 써주자.
    val networkService : NetworkService by lazy {
        ApplicationController.instance.networkService
    }


    // backpressed변수
    var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureBottomNavigation()
    }


    // 탭바 추가 함수
    private fun configureBottomNavigation() {

        // 뷰페이저 스와이프 막기 위해서, customviewpager사용
        val customViewPager = findViewById(R.id.vp_bottom_main_act_frag_pager) as MainFragmentViewPager
        customViewPager.setPagingEnabled(false)
        customViewPager.adapter = MyFragmentStatePagerAdapter(supportFragmentManager, 4)

        tl_bottom_main_act_bottom_menu.setupWithViewPager(vp_bottom_main_act_frag_pager)

        val bottomNaviLayout: View = this.layoutInflater.inflate(R.layout.bottom_navigation_tab, null, false)

        tl_bottom_main_act_bottom_menu.getTabAt(0)!!.customView =
                bottomNaviLayout.findViewById(R.id.btn_bottom_navi_main_tab) as RelativeLayout
        tl_bottom_main_act_bottom_menu.getTabAt(1)!!.customView =
                bottomNaviLayout.findViewById(R.id.btn_bottom_navi_map_tab) as RelativeLayout
        tl_bottom_main_act_bottom_menu.getTabAt(2)!!.customView =
                bottomNaviLayout.findViewById(R.id.btn_bottom_navi_my_page_tab) as RelativeLayout
        tl_bottom_main_act_bottom_menu.getTabAt(3)!!.customView =
                bottomNaviLayout.findViewById(R.id.btn_bottom_navi_cart_tab) as RelativeLayout
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


