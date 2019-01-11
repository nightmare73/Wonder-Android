package com.wonder.bring.Network.Get

data class GetDaumKeywordAddressResponseData(
    var meta: KeywordAddressMeta,
    var documents: ArrayList<KeywordAddressDocument>
)

data class KeywordAddressMeta(
    var total_count: Int,                   //검색어에 검색된 문서수
    var pageable_count: Int,                //total_count 중에 노출가능 문서수
    var is_end: Boolean,                    //현재 페이지가 마지막 페이지인지 여부. 값이 false이면 page를 증가시켜 다음 페이지를 요청할 수 있음.
    var same_name: KeywordAddressSameName   //질의어의 지역/키워드 분석 정보
)

data class KeywordAddressSameName(
    var region: ArrayList<String>,          //질의어에서 인식된 지역의 리스트. '중앙로 맛집' 에서 중앙로에 해당하는 지역 리스트
    var keyword: String,                    //질의어에서 지역 정보를 제외한 키워드. '중앙로 맛집' 에서 '맛집'
    var selected_region: String             //인식된 지역 리스트 중, 현재 검색에 사용된 지역 정보.
)

data class KeywordAddressDocument(
    var id: String,                         //장소 ID
    var place_name: String,                 //장소명, 업체명
    var category_name: String,              //장소명, 업체명
    var category_group_code: String,        //중요 카테고리만 그룹핑한 카테고리 그룹 코드. Request에 category_group_code 테이블 참고
    var category_group_name: String,        //중요 카테고리만 그룹핑한 카테고리 그룹명. Request에 category_group_code 테이블 참고
    var phone: String,                      //전화번호
    var address_name: String,               //전체 지번 주소
    var road_address_name: String,          //전체 도로명 주소
    var x: String,                          //X 좌표값 혹은 longitude
    var y: String,                          //Y 좌표값 혹은 latitude
    var place_url: String,                  //장소 상세페이지 URL
    var distance: String                    //중심좌표까지의 거리(x,y 파라미터를 준 경우에만 존재). 단위 meter
)

//category_group_code
//code	의미
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//MT1	대형마트
//CS2	편의점
//PS3	어린이집, 유치원
//SC4	학교
//AC5	학원
//PK6	주차장
//OL7	주유소, 충전소
//SW8	지하철역
//BK9	은행
//CT1	문화시설
//AG2	중개업소
//PO3	공공기관
//AT4	관광명소
//AD5	숙박
//FD6	음식점
//CE7	카페
//HP8	병원
//PM9	약국