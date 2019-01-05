package com.wonder.bring.MainFragment

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kakao.util.maps.helper.Utility

import com.wonder.bring.R
import kotlinx.android.synthetic.main.fragment_home.*
import net.daum.mf.map.api.*
import org.jetbrains.anko.support.v4.toast
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class HomeFragment : Fragment() {
    //이떄의 gps는 내가 한양대잇을때의 값을 가져왔음
    private var userLatitude: Double = 37.55818269968138
    private var userLongitude: Double = 127.04231960631296
    private var userGpsAccuracy: Float = 0.0f

    private val PERMISSIONS_ACCESS_FINE_LOCATION_REQUEST_CODE: Int = 1000

    private lateinit var mapView: MapView
    private lateinit var mapViewContainer: ViewGroup


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        requestGpsPermission()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mapInit()
        btn_goto_myposition.setOnClickListener {
            goToUserPosition()
        }

        btn_test_my_gps.setOnClickListener {
            setPinMyGps()
        }

        btn_delete_pin.setOnClickListener {
            mapView.removeAllPOIItems()
        }

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun mapInit() {

        //맵뷰 초기화
        mapView = MapView(activity)
        //맵뷰에 내 네이티브키 등록
        mapView.setDaumMapApiKey("238345100979e74d743629c451b3d561")


        //맵뷰를 담을 view가져오기
        mapViewContainer = mv_home_fragment_map
        //view에 맵뷰 담기
        mapViewContainer.addView(mapView)

        //해쉬키 알아오는 로그캣
        //여기는 프래그먼트니까 context가 없다.
        //context를 얻어오기 위해서 activity를 사용
        Log.v("Malibin hash", getKeyHash(activity!!.applicationContext))

    }

    //해쉬키 받아오기
    //앱에서 고유한 해쉬키가 있는듯 하다. 이 해쉬키를 다음 API 사이트에 등록을 안하면
    //지도에 다음 로고만 나오는 기현상이 발생한다.
    //뭐 이 함수는 한 번 쓰고 나면 필요가 없어지니 삭제하는 편도 나쁘지 않다.

    private fun getKeyHash(ctx: Context): String? {
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


    private fun requestGpsPermission() {
        //이전에 이미 권한 메세지에 대해 OK 했는지 검사
        if (ActivityCompat.checkSelfPermission(
                context!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.v("Malibin Debug", "권한 확인햇음")
            //이전에 권한을 요청한적이 없다면
            //권한을 요청하는 메세지 띄운다
            //액티비티에서는ActivityCompat.requestPermissions()를 호출하지만
            //프래그먼트에서는 requestPermissions()를 호출한다.
            //그래야 여기에있는  onRequestPermissionsResult()이 호출됨
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_ACCESS_FINE_LOCATION_REQUEST_CODE
            )
            Log.v("Malibin GPS", "GPS 권한 요청 띄움")

        } else {
            //기존에 권한을 허용했을경우 이 로직 동작
            Log.v("Malibin GPS", "기존에 GPS 권한을 허용했었음")
            getMyGPS()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //어떤 권한에 대한 callback 인지 물어본다
        //아래의 경우 requestGpsPermission() 에서 호출된 콜백임을 뜻한다.
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION_REQUEST_CODE) {

            Log.v("Malibin Debug", "requestGpsPermission()를 통해 들어옴")

            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //GPS권한을 허용했을경우 이 로직 동작
                Log.v("Malibin GPS", "방금 GPS 권한을 허용함")
                getMyGPS()
            } else {
                Log.v("Malibin GPS", "gps 권한 거부")
                //GPS권한을 거부했을경우 이 로직 동작
                //어플을 걍 꺼버린다
                activity!!.finish()
            }
        }
    }

    private fun getMyGPS() {

        //이 코드를 넣어줘야 location 할당하는 코드가 동작함....
        if (Build.VERSION.SDK_INT >= 23 &&
            ContextCompat.checkSelfPermission(
                activity!!.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                activity!!.applicationContext,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
        }

        val lm: LocationManager? = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location: Location? = lm!!.getLastKnownLocation(LocationManager.GPS_PROVIDER) as Location

        // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록
        lm.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,   //등록할 위치제공자
            100,                   //통지사이의 최소 시간간격 (miliSecond)
            1f,                 //통지사이의 최소 변경거리 (m)
            mLocationListener
        )

        userLatitude = location!!.latitude
        userLongitude = location!!.longitude
        userGpsAccuracy = location!!.accuracy

        Log.v(
            "Malibin GPS",
            location!!.latitude.toString() + ", " + location.longitude.toString() + ",  " + userGpsAccuracy
        )

    }

    private fun goToUserPosition() {
        var userPosition: CameraPosition =
            CameraPosition(MapPoint.mapPointWithGeoCoord(userLatitude, userLongitude), 2f)//머가 문제일까? 터지넹
        var userMapPoint: MapPoint = MapPoint.mapPointWithGeoCoord(userLatitude, userLongitude)

        //mapView.animateCamera(CameraUpdateFactory.newCameraPosition(userPosition))//머가 문제지 터지는게
        //mapView.moveCamera(CameraUpdateFactory.newMapPoint(userMapPoint)) //이건 잘 되긴하는데 정확하지가 않은듯??

        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        toast("goToUserPosition called, Position : " + userLatitude.toString() + ", " + userLongitude.toString() + ",  " + userGpsAccuracy)
        Log.v(
            "Malibin GPS",
            "goToUserPosition called, Position : " + userLatitude.toString() + ", " + userLongitude.toString() + ",  " + userGpsAccuracy
        )
    }

    private fun setPinMyGps() {
        var userMapPoint: MapPoint = MapPoint.mapPointWithGeoCoord(userLatitude, userLongitude)
        var poiItem: MapPOIItem = MapPOIItem()
        poiItem.itemName = "내 시스템 GPS 위치"
        poiItem.mapPoint = userMapPoint
        poiItem.markerType = MapPOIItem.MarkerType.YellowPin
        mapView.addPOIItem(poiItem)

        mapView.moveCamera(CameraUpdateFactory.newMapPoint(userMapPoint))
        Log.v(
            "Malibin GPS",
            "setPinMyGps called, Position : " + userLatitude.toString() + ", " + userLongitude.toString() + ",  " + userGpsAccuracy
        )
        toast("setPinMyGps called, Position : " + userLatitude.toString() + ", " + userLongitude.toString() + ",  " + userGpsAccuracy)
    }


    private var mLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            //위치값이 갱신되면 이 이벤트 발생
            userLatitude = location!!.latitude
            userLongitude = location!!.longitude
            userGpsAccuracy = location!!.accuracy
            Log.v(
                "Malibin GPS",
                "mLocationListener called, Position : " + userLatitude.toString() + ", " + userLongitude.toString() + ",  " + userGpsAccuracy
            )
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        }

        override fun onProviderEnabled(p0: String?) {
        }

        override fun onProviderDisabled(p0: String?) {
        }
    }

}
