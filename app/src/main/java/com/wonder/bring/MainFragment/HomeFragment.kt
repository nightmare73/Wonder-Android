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
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.bumptech.glide.Glide
import com.kakao.util.maps.helper.Utility
import com.wonder.bring.Network.ApplicationController
import com.wonder.bring.Network.DaumService
import com.wonder.bring.Network.Get.GetDaumKeywordAddressResponseData
import com.wonder.bring.Network.Get.GetSelectedStoreSummaryResponseData
import com.wonder.bring.Network.Get.GetStoreLocationAroundUserResponseData
import com.wonder.bring.Network.Get.OtherDataClasses.StoreLocation
import com.wonder.bring.Network.Get.OtherDataClasses.StoreSummary
import com.wonder.bring.Network.NetworkService

import com.wonder.bring.R
import com.wonder.bring.StoreProcess.StoreActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import net.daum.android.map.MapViewTouchEventListener
import net.daum.mf.map.api.*
import org.jetbrains.anko.MAXDPI
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class HomeFragment : Fragment(), MapView.POIItemEventListener, MapView.MapViewEventListener {

    private var debugCnt: Int = 0
    private var DEBUG_MODE: Boolean = false

    //이떄의 gps는 내가 한양대잇을때의 값을 가져왔음
    //37.558182 127.042319
    private var userLatitude: Double = 37.55818269968138
    private var userLongitude: Double = 127.04231960631296
    private var userGpsAccuracy: Float = 0.0f

    private val PERMISSIONS_ACCESS_FINE_LOCATION_REQUEST_CODE: Int = 1000

    private lateinit var mapView: MapView
    private lateinit var mapViewContainer: ViewGroup

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

        debuggingMode()

    }

    private fun debuggingMode() {

        et_home_fragment_searchbar.setOnClickListener {
            debugCnt++
            if (debugCnt >= 10) {

                btn_test_my_gps.visibility = View.VISIBLE
                btn_delete_pin.visibility = View.VISIBLE
                btn_maru.visibility = View.VISIBLE
                btn_univ.visibility = View.VISIBLE

                btn_test_my_gps.setOnClickListener {
                    setPinMyGps()
                }

                btn_delete_pin.setOnClickListener {
                    mapView.removeAllPOIItems()
                }

                //"37.495426", "127.038843"//테스트용 좌표 브링카페
                btn_maru.setOnClickListener {
                    DEBUG_MODE = true
                    setPoiItemsAroundMyLocation(37.495426, 127.038843)
                }

                //37.596322, 127.052640 테스트용 경희대 근처 카페
                btn_univ.setOnClickListener {
                    DEBUG_MODE = true
                    setPoiItemsAroundMyLocation(37.596322, 127.052640)
                }
            }
        }
    }

    //----------------------상속받은 MapView.POIItemEventListener 인터페이스 구현코드----------------------------------------

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {

    }

    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {

    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {

    }

    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {


        //이거 한번 더 누르면 터져버림 ㅋㅋ;;
//        var tempMapPOIItem = MapPOIItem()
//        tempMapPOIItem.customImageResourceId = R.drawable.pin_icon_clicked
//        tempMapPOIItem.markerType = MapPOIItem.MarkerType.CustomImage
//        tempMapPOIItem.mapPoint = p1!!.mapPoint
//        tempMapPOIItem.itemName = ""
//        tempMapPOIItem.isShowCalloutBalloonOnTouch = false
//        mapView.addPOIItem(tempMapPOIItem)

        var userObject: StoreLocation = p1!!.userObject as StoreLocation
        networkService.getSelectedStoreSummaryRequest("application/json", userObject.storeIdx)
            .enqueue(object : Callback<GetSelectedStoreSummaryResponseData> {
                override fun onFailure(call: Call<GetSelectedStoreSummaryResponseData>, t: Throwable) {
                    toast("서버에서 매장 정보를 불러오지 못하였습니다.")
                }

                override fun onResponse(
                    call: Call<GetSelectedStoreSummaryResponseData>,
                    response: Response<GetSelectedStoreSummaryResponseData>
                ) {
                    Log.v("Malibin Debug", "서버에서 온 매장 서머리 데이터 : " + response.body()!!.toString())
                    cl_home_fragment_summary.visibility = View.VISIBLE
                    cl_home_fragment_summary.tv_home_fragment_store_name.text = response.body()!!.data.name
                    cl_home_fragment_summary.tv_home_fragment_etc_info.text =
                            (response.body()!!.data.address + "   " + userObject.distance + "m")
                    cl_home_fragment_summary.tv_home_fragment_store_address.text = response.body()!!.data.address
                    cl_home_fragment_summary.tv_home_fragment_store_phone_number.text = response.body()!!.data.number

                    Glide.with(this@HomeFragment).load(response.body()!!.data.photoUrl[0])
                        .into(cl_home_fragment_summary.iv_home_fragment_menu1)
                    Glide.with(this@HomeFragment).load(response.body()!!.data.photoUrl[1])
                        .into(cl_home_fragment_summary.iv_home_fragment_menu2)
                    Glide.with(this@HomeFragment).load(response.body()!!.data.photoUrl[2])
                        .into(cl_home_fragment_summary.iv_home_fragment_menu3)

                    cl_home_fragment_summary.btn_home_fragment_store_detail.setOnClickListener {
                        startActivity<StoreActivity>(
                            "storeIdx" to userObject.storeIdx,
                            "storeName" to response.body()!!.data.name,
                            "storeAdress" to response.body()!!.data.address
                        )
                    }
                }


            })

    }


    //--------------------------------------------------------------------------------------------------------------------

    //----------------------상속받은 MapView.MapViewEventListener 인터페이스 구현코드----------------------------------------

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewInitialized(p0: MapView?) {
    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
        cl_home_fragment_summary.visibility = View.GONE
    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
    }

    //----------------------------------------------------------------------------------------------------------------------


    private fun mapInit() {

        //맵뷰 초기화
        mapView = MapView(activity)
        //맵뷰에 내 네이티브키 등록
        mapView.setDaumMapApiKey("238345100979e74d743629c451b3d561")


        //맵뷰를 담을 view가져오기
        mapViewContainer = mv_home_fragment_map
        //view에 맵뷰 담기
        mapViewContainer.addView(mapView)
        //내위치 화면에 표시
        //mapView.setShowCurrentLocationMarker(true)

        //이벤트 리스너 등록
        //여기안에다가 그냥 바로 인터페이스 정의해서 넣어줬더니 클릭리스너가 안먹힌다.
        //그래서 현재 클래스에 상속을 받아버리고 이 클래스를 집어넣었다.....ㅅㅂ
        mapView.setPOIItemEventListener(this)

        //얘도 마찬가지...^^;;;
        mapView.setMapViewEventListener(this)

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

            DEBUG_MODE = false
            setPoiItemsAroundMyLocation(userLatitude, userLongitude)
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
        val isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

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

            //현위치 마커찍어주는게 안먹어서 그냥 트래킹모드를 사용한다. 현위치 마커만 변동되고 맵이움직이거나 하지는 않음.
            //커스텀 이미지를 등록해놧더니 와우;; 애니메이션이 자동으로 생겻다 다음 감사합니다 ^^;;;
            mapView.currentLocationTrackingMode =
                    MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving
            mapView.setCustomCurrentLocationMarkerTrackingImage(R.drawable.current_location, MapPOIItem.ImageOffset(16, 33))
            mapView.setShowCurrentLocationMarker(true)


            //getLastKnownLocation에 좌표가 없는 경우를 생각해서 포문을 돌린다
            var providers: List<String> = lm.getProviders(true)
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
            userLongitude = location.longitude
            userGpsAccuracy = location.accuracy

            Log.v(
                "Malibin GPS",
                location.latitude.toString() + ", " + location.longitude.toString() + ",  " + userGpsAccuracy
            )

        }
        //GPS 안켜져있으면 켜달라는 토스트 메세지 띄우기
        else {
            toast("GPS를 체크해주세요")
        }

    }

    private fun setPoiItemsAroundMyLocation(inputLatitude: Double, inputLongitude: Double) {

        var poiItemsAroundMyLocation: ArrayList<MapPOIItem> = ArrayList()
        val tmp = MapPOIItem()

        networkService.getStoreLocationAroundUserRequest(
            "application/json",
            String.format("%.6f", inputLatitude),
            String.format("%.6f", inputLongitude)
        ).enqueue(object : Callback<GetStoreLocationAroundUserResponseData> {
            override fun onFailure(call: Call<GetStoreLocationAroundUserResponseData>, t: Throwable) {
                toast("서버에서 주변 매장 위치를 불러오는데 실패하였습니다.")
            }

            override fun onResponse(
                call: Call<GetStoreLocationAroundUserResponseData>,
                response: Response<GetStoreLocationAroundUserResponseData>
            ) {
                if (response.body()!!.data != null) {
                    //마커마다 각각 설정 해주는 반복문
                    for (data in response.body()!!.data) {
                        var userMapPoint: MapPoint = MapPoint.mapPointWithGeoCoord(data.latitude, data.longitude)
                        var tempMapPOIItem = MapPOIItem()
                        tempMapPOIItem.customImageResourceId = R.drawable.pin_icon
                        tempMapPOIItem.markerType = MapPOIItem.MarkerType.CustomImage

                        tempMapPOIItem.isShowDisclosureButtonOnCalloutBalloon = false

                        tempMapPOIItem.mapPoint = userMapPoint
                        tempMapPOIItem.userObject = data
                        tempMapPOIItem.itemName = data.storeName

                        poiItemsAroundMyLocation.add(tempMapPOIItem)
                    }
                    //서버에서 받은 데이터를 가지고 전역변수 POIItem들을 초기화 시켰으니 그 객체들을 가지고 지도에 뿌린다.

                }
                setMapviewWithStoreLocation(poiItemsAroundMyLocation)

                toast("서버에서 가져온 데이터 : " + response.body().toString())
                Log.v("Malibin Debug", "setPoiItemsAroundMyLocation() onResponse 함수 끝 ")
            }
        })
    }

    private fun setMapviewWithStoreLocation(poiItemsAroundMyLocation: ArrayList<MapPOIItem>) {
        mapView.removeAllPOIItems()
        //여기서 맵뷰에다가 직접 POIItem들을 다 집어넣고 (맵에 실제로 보여짐)
        //그 위치로 카메라를 이동
        if (poiItemsAroundMyLocation.size > 0) {
            //내주변 매장 조회 데이터가 존재하면 동작

            for (data in poiItemsAroundMyLocation) {
                mapView.addPOIItem(data)
            }

            if (DEBUG_MODE) {
                //사실 이 카메라가 내 현재 좌표가 되어야함.
                mapView.moveCamera(CameraUpdateFactory.newMapPoint(poiItemsAroundMyLocation[0].mapPoint))
            } else {
                setCameraPosition(userLatitude, userLongitude)
            }

        } else {
            setCameraPosition(userLatitude, userLongitude)
            toast("내 주변 매장이 존재하지 않습니다.")
            Log.v("Malibin Debug", "storeData 가 0개임")
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

    private fun setCameraPosition(latitude: Double, longitude: Double) {
        var destinationPoint: MapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude)
        mapView.moveCamera(CameraUpdateFactory.newMapPoint(destinationPoint))
    }

    private var mLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            //위치값이 갱신되면 이 이벤트 발생
            userLatitude = location!!.latitude
            userLongitude = location.longitude
            userGpsAccuracy = location.accuracy
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
