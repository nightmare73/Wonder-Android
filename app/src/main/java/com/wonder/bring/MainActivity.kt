package com.wonder.bring

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.NetworkService
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import android.util.Log
import com.wonder.bring.LoginProcess.LoginActivity
import com.wonder.bring.MainFragment.*
import com.wonder.bring.Network.Get.GetTokenValidationResponseData
import com.wonder.bring.db.SharedPreferenceController
import org.jetbrains.anko.startActivityForResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    var isLogin = false
    var nick = ""

    lateinit var pa: MyFragmentStatePagerAdapter

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
        loginCheck()
    }

    // 탭바 추가 함수
    private fun configureBottomNavigation() {

        // 뷰페이저 스와이프 막기 위해서, customviewpager사용
        val customViewPager = findViewById(R.id.vp_bottom_main_act_frag_pager) as MainFragmentViewPager
        customViewPager.setPagingEnabled(false)
        pa = MyFragmentStatePagerAdapter(supportFragmentManager, 4)
        customViewPager.adapter = pa
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

    fun loginCheck() {

        var userToken: String = SharedPreferenceController.getAuthorization(this)

        if (userToken.equals("")) {

            isLogin = false
            Log.v("Malibin Debug", "애초에 토큰이 : $userToken")

        } else {

            networkService.getTokenValidationRequest(userToken).enqueue(object :
                Callback<GetTokenValidationResponseData> {
                override fun onFailure(call: Call<GetTokenValidationResponseData>, t: Throwable) {
                    toast("서버 통신에 실패하였습니다.")
                }

                override fun onResponse(
                    call: Call<GetTokenValidationResponseData>,
                    response: Response<GetTokenValidationResponseData>
                ) {
                    Log.v("Malibin Debug", "응답은 : " + response.body().toString())
                    if (response.isSuccessful) {

                        val branch = response.body()!!.status

                        when (branch) {
                            200 -> {
                                //유효한 토큰인 경우
                                isLogin = true
                                Log.v("Malibin Debug", "유효한 토큰임 로그인 성공")
                            }
                            else -> {
                                //토큰은 잇지만 요휴한게 아닌경우
                                Log.v("Malibin Debug", "유효하지않은 토큰임 로그인 실패")
                                isLogin = false
                            }
                        }
                    }
                    settingView(isLogin)

                }
            })

        }
    }

    fun settingView(isOk: Boolean) {
        //OrderhistoryFragment
        (pa.getItem(1) as OrderhistoryFragment).viewToggle(isOk)
        //CartFragment
        (pa.getItem(2) as CartFragment).viewToggle(isOk)
        //MypageFragment
        (pa.getItem(3) as MypageFragment).viewToggle(isOk)
    }

    fun callLoginAct() {

        startActivityForResult<LoginActivity>(1000)
    }

    //로그인 액티비티에서 다시 액티비티로 올때 실행
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000) {

            Log.v("Malibin Debug", "onActivityResult에 requestCode==1000 으로 들어옴")
            if (resultCode == Activity.RESULT_OK) {
                Log.v("Malibin Debug", "onActivityResult에 requestCode==1000 으로 들어와서 Activity.RESULT_OK임")
                settingView(true)
            } else
                settingView(false)

        }
    }




}






