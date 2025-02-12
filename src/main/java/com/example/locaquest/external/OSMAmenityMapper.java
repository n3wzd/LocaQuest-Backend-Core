package com.example.locaquest.external;

import java.util.*;

public class OSMAmenityMapper {

    private static final Map<String, List<String>> amenityMap = new HashMap<>();

    static {
        putData(new String[] { "음식", "식사", "식당", "음식점", "외식", "브런치" }, "restaurant");
        putData(new String[] { "카페", "커피숍", "디저트" }, "cafe");
        putData(new String[] { "패스트푸드", "식당" }, "fast_food");
        putData(new String[] { "바", "술집", "주점", "펍" }, "bar");
        putData(new String[] { "바", "술집", "주점", "펍" }, "pub");
        putData(new String[] { "아이스크림", "빙수", "디저트", "간식" }, "ice_cream");

        putData(new String[] { "학교", "초등학교", "중학교", "고등학교", "교육기관" }, "school");
        putData(new String[] { "유치원", "어린이집", "유아교육", "보육원" }, "kindergarten");
        putData(new String[] { "도서관", "도서실", "책방", "공공도서관" }, "library");
        putData(new String[] { "대학교", "대학" }, "university");

        putData(new String[] { "병원", "종합병원", "클리닉", "의료기관", "진료소" }, "hospital");
        putData(new String[] { "약국", "약방", "약재점", "약소" }, "pharmacy");
        putData(new String[] { "치과", "치과의원", "치과병원", "병원" }, "dentist");
        putData(new String[] { "클리닉", "의원", "진료소", "병원" }, "clinic");

        putData(new String[] { "은행", "금융" }, "bank");
        putData(new String[] { "ATM", "현금인출기" }, "atm");
        putData(new String[] { "환전소", "환전기", "외환소" }, "bureau_de_change");

        putData(new String[] { "주유소", "주유" }, "fuel");
        putData(new String[] { "주차", "주차장", "주차시설", "주차공간" }, "parking");
        putData(new String[] { "택시", "콜택시" }, "taxi");
        putData(new String[] { "버스", "터미널", "정류장" }, "bus_station");

        putData(new String[] { "소방서", "소방" }, "fire_station");
        putData(new String[] { "경찰서", "경찰" }, "police");
        putData(new String[] { "우체국", "우편" }, "post_office");
        putData(new String[] { "시청", "구청", "군청", "행정센터" }, "townhall");

        putData(new String[] { "영화관", "극장", "영화" }, "cinema");
        putData(new String[] { "극장", "뮤지컬", "공연" }, "theatre");
        putData(new String[] { "카지노" }, "casino");
        putData(new String[] { "박물관", "미술관" }, "arts_centre");
    }

    private static void putData(String[] words, String keyword) {
        for (String word : words) {
            amenityMap.computeIfAbsent(word, _ -> new ArrayList<>()).add(keyword);
        }
    }

    public static List<String> getAmenityList(String word) {
        return amenityMap.getOrDefault(word, new ArrayList<>());
    }
}
