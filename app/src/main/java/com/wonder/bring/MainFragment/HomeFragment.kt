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
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.kakao.util.maps.helper.Utility
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.DaumService
import com.wonder.bring.Network.Get.GetDaumAdressResponseData
import com.wonder.bring.Network.Get.GetDaumKeywordAddressResponseData
import com.wonder.bring.Network.Get.GetStoreLocationAroundUserResponseData
import com.wonder.bring.Network.NetworkService

import com.wonder.bring.R
import kotlinx.android.synthetic.main.fragment_home.*
import net.daum.mf.map.api.*
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

    private var poiItemsAroundMyLocation: ArrayList<MapPOIItem> = ArrayList()
    private var poiItem: MapPOIItem = MapPOIItem()

    // 보미 서버 통신
    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }
    // 다음 서버 통신
    val daumService: DaumService by lazy {
        ApplicationController.instance.daumService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //키자마자 내 위치 갱신
        //키자마자 GPS가 안켜져있었다면 내위치가 갱신이 안됨. 다른 버튼을 눌러서 내 위치를 잡아주거나 해야함


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mapInit()
        searchbarInit()
        buttonInit()
        requestGpsPermission()

        btn_test_my_gps.setOnClickListener {
            setPinMyGps()
        }

        btn_delete_pin.setOnClickListener {
            mapView.removeAllPOIItems()
        }

        btn_comon.setOnClickListener {


            cl_home_fragment_summary.visibility = View.VISIBLE
        }

        btn_getout.setOnClickListener {


            cl_home_fragment_summary.visibility = View.GONE
        }

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

        mapView.setPOIItemEventListener(object: MapView.POIItemEventListener{
            override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onCalloutBalloonOfPOIItemTouched(
                p0: MapView?,
                p1: MapPOIItem?,
                p2: MapPOIItem.CalloutBalloonButtonType?
            ) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            //핀이 눌렷을 때 이벤트 처리
            override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                // 여기서 MapPOIItem 눌린놈 식별할수잇는 id 값을 전역변수에 저장해놓고 이게 다시 호추될때마다 핀을 바꿔줘야겠다
            }


        })


        //해쉬키 알아오는 로그캣
        //여기는 프래그먼트니까 context가 없다.
        //context를 얻어오기 위해서 activity를 사용
        Log.v("Malibin hash", getKeyHash(activity!!.applicationContext))

    }

    private fun searchbarInit() {

        var userInput: String

        et_home_fragment_searchbar.setOnEditorActionListener { textView, actionId, keyEvent ->

            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    userInput = textView.text.toString()

                    //DAUM REST API 주소 키워드 검색 코드
                    daumService.getDaumKeywordAdressRequest(
                        "KakaoAK ab0af3bcabe549eb390aa05e734417f5",
                        userInput, 1, 10
                    ).enqueue(object : Callback<GetDaumKeywordAddressResponseData> {
                        override fun onFailure(call: Call<GetDaumKeywordAddressResponseData>, t: Throwable) {
                            toast("검색에 실패하였습니다.")
                        }

                        override fun onResponse(
                            call: Call<GetDaumKeywordAddressResponseData>,
                            response: Response<GetDaumKeywordAddressResponseData>
                        ) {
                            Log.v("Malibin Debug : ", "Daum Keyword Address Request = " + response.body().toString())
                            var string: String = ""
                            for (result in response.body()!!.documents) {
                                var temp: String = result.place_name + " = " + result.address_name + "\n"
                                string += temp
                            }
                            toast(string).duration = Toast.LENGTH_LONG
                            Log.v("Malibin Debug : ", "Daum Keyword Address Request = $string")
                        }

                    })

                    true
                }

                else -> false
            }


        }

    }

    private fun buttonInit() {
        //내위치
        btn_home_fragment_search_mylocation.setOnClickListener {

            setMapviewWithStoreLocation()

            toast(String.format("%.6f", userLatitude) + ", " + String.format("%.6f", userLongitude))

        }
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
        Log.v("Malibin Debug", "getMyGPS() 호출됨")

        val lm: LocationManager? = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        Log.v("Malibin Debug", "getMyGPS() location manager 객체 생성 통과")

        val isGPSEnabled = lm!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = lm!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        //GPS가 켜져잇는지 안켜져있는지 확인
        if (isGPSEnabled || isNetworkEnabled) {
            //이 코드를 넣어줘야 location 할당하는 코드가 동작함....
            if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(
                    activity!!.applicationContext,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
            }
            Log.v("Malibin Debug", "getMyGPS() 아무의미없는 코드 통과")


            //getLastKnownLocation에 좌표가 없는 경우를 생각해서 포문을 돌린다
            var providers: List<String> = lm!!.getProviders(true)
            var location: Location? = null
            for (provider in providers) {
                var l = lm.getLastKnownLocation(provider)
                if (l == null) {
                    continue
                }
                if (location == null || l.getAccuracy() < location.getAccuracy()) {
                    location = l
                }
            }

            //val location: Location? = lm!!.getLastKnownLocation(LocationManager.GPS_PROVIDER) as? Location
            Log.v("Malibin Debug", "getMyGPS() location 객체 생성 통과")

            // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록
            lm.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,   //등록할 위치제공자
                100,                   //통지사이의 최소 시간간격 (miliSecond)
                1f,                 //통지사이의 최소 변경거리 (m)
                mLocationListener
            )
            lm.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,  // 등록할 위치제공자
                100,                      // 통지사이의 최소 시간간격 (miliSecond)
                1f,                    // 통지사이의 최소 변경거리 (m)
                mLocationListener
            )
            Log.v("Malibin Debug", "getMyGPS() lm에 리스너 등록 통과")

            userLatitude = location!!.latitude
            userLongitude = location!!.longitude
            userGpsAccuracy = location!!.accuracy

            Log.v(
                "Malibin GPS",
                location!!.latitude.toString() + ", " + location.longitude.toString() + ",  " + userGpsAccuracy
            )
        }
        //GPS 안켜져있으면 켜달라는 토스트 메세지 띄우기
        else {
            toast("GPS를 체크해주세요")
        }

    }

    private fun getPoiItemsAroundMyLocation() {

        networkService.getStoreLocationAroundUserRequest(
            "application/json",
            //String.format("%.6f", userLatitude),
            // String.format("%.6f", userLongitude)
            "37.495426", "127.038843"//테스트용 좌표 브링카페
        ).enqueue(object : Callback<GetStoreLocationAroundUserResponseData> {
            override fun onFailure(call: Call<GetStoreLocationAroundUserResponseData>, t: Throwable) {
                toast("주변 매장 위치를 불러오는데 실패하였습니다.")
            }

            override fun onResponse(
                call: Call<GetStoreLocationAroundUserResponseData>,
                response: Response<GetStoreLocationAroundUserResponseData>
            ) {
                Log.v("Malibin Debug","getPoiItemsAroundMyLocation() 서버통신 성공 데이터는 \n"+response.body().toString())
                if (response.body()!!.data != null) {
                    for (data in response.body()!!.data)
                    {
                        var userMapPoint: MapPoint = MapPoint.mapPointWithGeoCoord(data.latitude, data.longitude)
                        var tempMapPOIItem = MapPOIItem()
                        tempMapPOIItem.customImageResourceId = R.drawable.pin_icon
                        tempMapPOIItem.markerType = MapPOIItem.MarkerType.CustomImage
                        tempMapPOIItem.mapPoint = userMapPoint
                        tempMapPOIItem.userObject = data
                        tempMapPOIItem.itemName = data.storeIdx.toString()

                        poiItemsAroundMyLocation.add(tempMapPOIItem)
                    }
                }
                Log.v("Malibin Debug","getPoiItemsAroundMyLocation() onResponse 함수 끝 ")
            }
        })
    }

    private fun setMapviewWithStoreLocation(){

        getPoiItemsAroundMyLocation()

        //if(poiItemsAroundMyLocation.size < 0)


        if(poiItemsAroundMyLocation.size > 0) {
            Log.v("Malibin Debug","storeData가 1개 이상이여서 동작")
            for (data in poiItemsAroundMyLocation) {
                Log.v("Malibin Debug", data.toString())
            }

            for (data in poiItemsAroundMyLocation) {
                mapView.addPOIItem(data)
                Log.v("Malibin Debug", data.toString())
            }
            mapView.moveCamera(CameraUpdateFactory.newMapPoint(poiItemsAroundMyLocation[0].mapPoint))

        }
        else{
            toast("좌표 아이템이 존재하지않음.")
            Log.v("Malibin Debug","storeData 가 0개임")
        }

    }


    private fun setPinMyGps() {
        var userMapPoint: MapPoint = MapPoint.mapPointWithGeoCoord(userLatitude, userLongitude)

        //커스텀 핀 등록
        poiItem.customImageResourceId = R.drawable.pin_icon

        poiItem.itemName = "내 시스템 GPS 위치"
        poiItem.mapPoint = userMapPoint
        poiItem.markerType = MapPOIItem.MarkerType.CustomImage

        mapView.addPOIItem(poiItem)

//        var btn: Button =  poiItem.customImageResourceId

        //원하는 좌표로
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
