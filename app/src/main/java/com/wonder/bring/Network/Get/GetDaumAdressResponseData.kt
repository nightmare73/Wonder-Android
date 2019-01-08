package com.wonder.bring.Network.Get

data class GetDaumAdressResponseData(
    var meta: AddressMeta,
    var documents: ArrayList<AddressDocument>
)

data class AddressMeta(
    var total_count: Int,               //검색어에 검색된 문서수
    var pageable_count: Int,            //total_count 중에 노출가능 문서수
    var is_end: Boolean                 //현재 페이지가 마지막 페이지인지 여부. 값이 false이면 page를 증가시켜 다음 페이지를 요청할 수 있음.
)

data class AddressDocument(
    var address_name: String,           //전체 지번 주소 또는 전체 도로명 주소. (input에 따라 결정됨)
    var address_type: String,           //address_name의 값의 type. 지명, 도로명, 지번주소, 도로명주소 여부
    var x: String,                      //X 좌표값 혹은 longitude
    var y: String,                      //Y 좌표값 혹은 latitude
    var address: Address,               //지번주소 상세 정보
    var road_address: RoadAddress       //도로명주소 상세 정보
)

data class Address(
    var address_name: String,           //전체 지번 주소
    var region_1depth_name: String,     //지역 1Depth명 - 시도 단위
    var region_2depth_name: String,     //지역 2Depth명 - 구 단위
    var region_3depth_name: String,     //지역 3Depth명 - 동 단위
    var region_3depth_h_name: String,   //지역 3Depth 행정동 명칭
    var h_code: String,                 //행정 코드
    var b_code: String,                 //법정 코드
    var mountain_yn: String,            //산 여부
    var main_address_no: String,        //지번 주 번지
    var sub_address_no: String,         //지번 부 번지. 없을 경우 ""
    var zip_code: String,               //우편번호 (6자리)
    var x: String,                      //X 좌표값 혹은 longitude
    var y: String                       //Y 좌표값 혹은 latitude
)

data class RoadAddress(
    var address_name: String,
    var region_1depth_name: String,     //전체 도로명 주소
    var region_2depth_name: String,     //지역명1
    var region_3depth_name: String,     //지역명2
    var road_name: String,              //지역명3
    var underground_yn: String,         //도로명
    var main_building_no: String,       //지하여부 Y or N
    var sub_building_no: String,        //건물 본번
    var building_name: String,          //건물 부번 없을경우 ""
    var zone_no: String,                //우편번호 5자리
    var x: String,                      //X 좌표값 혹은 longitude
    var y: String                       //Y 좌표값 혹은 latitude
)