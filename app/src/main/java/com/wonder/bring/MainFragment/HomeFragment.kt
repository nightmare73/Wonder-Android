package com.wonder.bring.MainFragment

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kakao.util.maps.helper.Utility

import com.wonder.bring.R
import kotlinx.android.synthetic.main.fragment_home.*
import net.daum.mf.map.api.MapView
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class HomeFragment : Fragment() {

    private lateinit var mapView : MapView

    private lateinit var mapViewContainer: ViewGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mapInit()
    }

    private fun mapInit(){

        //맵뷰 초기화
        mapView = MapView(activity)
        //맵뷰에 내 네이티브키 등록
        mapView.setDaumMapApiKey("238345100979e74d743629c451b3d561")

        //맵뷰를 담을 view가져오기
        mapViewContainer = mv_home_fragment_map
        //view에 맵뷰 담기
        mapViewContainer.addView(mapView)

        //해쉬키 알아오는 로그캣
        Log.v("myhash",getKeyHash(activity!!.applicationContext))
    }

    //해쉬키 받아오기
    //앱에서 고유한 해쉬키가 있는듯 하다. 이 해쉬키를 다음 API 사이트에 등록을 안하면
    //지도에 다음 로고만 나오는 기현상이 발생한다.
    //뭐 이 함수는 한 번 쓰고 나면 필요가 없어지니 삭제하는 편도 나쁘지 않다.
    fun getKeyHash(ctx: Context): String? {
        var packageInfo: PackageInfo? = Utility.getPackageInfo(ctx, PackageManager.GET_SIGNATURES)

        if (packageInfo == null)
            return null

        for (signature in packageInfo.signatures) {
            try {
                var md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
        }
        return null
    }

}
