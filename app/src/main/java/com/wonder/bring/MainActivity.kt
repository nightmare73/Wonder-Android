package com.wonder.bring

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.wonder.bring.MainFragment.MyFragmentStatePagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureBottomNavigation()
    }


    private fun configureBottomNavigation() {
        vp_bottom_main_act_frag_pager.adapter = MyFragmentStatePagerAdapter(supportFragmentManager, 4)

        tl_bottom_main_act_bottom_menu.setupWithViewPager(vp_bottom_main_act_frag_pager)

        val bottomNaviLayout: View = this.layoutInflater.inflate(R.layout.bottom_navigation_tab, null, false)

        tl_bottom_main_act_bottom_menu.getTabAt(0)!!.customView = bottomNaviLayout.findViewById(R.id.btn_bottom_navi_main_tab) as RelativeLayout
        tl_bottom_main_act_bottom_menu.getTabAt(1)!!.customView = bottomNaviLayout.findViewById(R.id.btn_bottom_navi_map_tab) as RelativeLayout
        tl_bottom_main_act_bottom_menu.getTabAt(2)!!.customView = bottomNaviLayout.findViewById(R.id.btn_bottom_navi_my_page_tab) as RelativeLayout
        tl_bottom_main_act_bottom_menu.getTabAt(3)!!.customView=bottomNaviLayout.findViewById(R.id.btn_bottom_navi_cart_tab) as RelativeLayout
    }
}
