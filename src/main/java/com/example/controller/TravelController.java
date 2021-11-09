package com.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.Hotel;
import com.example.entity.TravelDestination;
import com.example.repository.HotelRepository;
import com.example.repository.TravelDestinationRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.ServerResponse.SseBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@RestController
@RequestMapping(value = "/travel")
public class TravelController {

    @Autowired
    TravelDestinationRepository tdRepository;

    @Autowired
    HotelRepository hRepository;

    @Value("${tourapi.key}")
    private String tourApiKey;

    // 지역코드조회
    @GetMapping(value = "/tourapi/city")
    public Map<String, Object> gettourapicity(@RequestParam(name = "cnt", defaultValue = "1") String cnt,
            @RequestParam("page") String page, @RequestParam(name = "areacode", defaultValue = "") String areacode) {
        Map<String, Object> map = new HashMap<>();
        try {

            String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaCode?serviceKey=" + tourApiKey
                    + "&numOfRows=" + cnt + "&pageNo=" + page + "&MobileOS=ETC&MobileApp=AppTest&areaCode=" + areacode
                    + "&_type=json";

            OkHttpClient client = new OkHttpClient();

            Request.Builder builder = new Request.Builder().url(url).get();
            Request request = builder.build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();

                if (body != null) {
                    String bodyString = body.string();
                    // System.out.println("Response :" + bodyString);
                    JSONObject jsonObject = new JSONObject(bodyString);
                    JSONArray j1 = jsonObject.getJSONObject("response").getJSONObject("body").getJSONObject("items")
                            .getJSONArray("item");
                    // System.out.println(j1);
                    List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
                    for (int i = 0; i < j1.length(); i++) {
                        Map<String, Object> a = new HashMap<>();
                        a.put("code", j1.getJSONObject(i).getInt("code"));
                        a.put("name", j1.getJSONObject(i).getString("name"));
                        list1.add(a);

                    }
                    map.put("list", list1);
                }
            } else
                System.err.println("Error Occurred");
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;

    }

    // tourapi [국문]
    @GetMapping(value = "/tourapi/select")
    public Map<String, Object> gettourapi(@RequestParam("page") String page, @RequestParam("cnt") String cnt,
            @RequestParam("arrange") String arrange, @RequestParam("contentTypeId") String contentTypeId,
            @RequestParam("areaCode") String areaCode) {
        Map<String, Object> map = new HashMap<>();
        try {
            String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?serviceKey="
                    + tourApiKey + "&pageNo=" + page + "&numOfRows=" + cnt + "&MobileApp=AppTest&MobileOS=ETC&arrange="
                    + arrange + "&contentTypeId=" + contentTypeId + "&areaCode=" + areaCode + "&listYN=Y&_type=json";

            OkHttpClient client = new OkHttpClient();

            Request.Builder builder = new Request.Builder().url(url).get();
            Request request = builder.build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();

                if (body != null) {
                    String bodyString = body.string();
                    // System.out.println("Response :" + bodyString);
                    JSONObject jsonObject = new JSONObject(bodyString);

                    JSONArray j1 = jsonObject.getJSONObject("response").getJSONObject("body").getJSONObject("items")
                            .getJSONArray("item");
                    // System.out.println(j1.getJSONObject(1));
                    List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
                    for (int i = 0; i < j1.length(); i++) {
                        Map<String, Object> a = new HashMap<>();
                        a.put("title", j1.getJSONObject(i).getString("title"));
                        a.put("addr", j1.getJSONObject(i).getString("addr1"));
                        a.put("areacode", j1.getJSONObject(i).getInt("areacode"));
                        a.put("contentid", j1.getJSONObject(i).getInt("contentid"));
                        a.put("firstimage", j1.getJSONObject(i).getString("firstimage"));
                        a.put("mapx", j1.getJSONObject(i).getFloat("mapx"));
                        a.put("mapy", j1.getJSONObject(i).getFloat("mapy"));
                        list1.add(a);

                    }
                    // System.out.println(list1);
                    map.put("list", list1);
                }
            } else
                System.err.println("Error Occurred");

            map.put("status", 200);
            // System.out.println(map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;

    }

    // 미구현 장소 추가 기능
    @PostMapping(value = "/insert", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> tdInsert(@RequestBody TravelDestination td, @RequestBody String type,
            @RequestBody Hotel hotel) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (type.equals("travelDestination")) {
                TravelDestination newtd = new TravelDestination();
                newtd.setTTitle(td.getTTitle());
                newtd.setTxLocation(td.getTxLocation());
                newtd.setTyLocation(td.getTyLocation());
                tdRepository.save(newtd);
                map.put("result", "여행지등록완료");
            }
            if (type.equals("hotel")) {
                Hotel newhotel = new Hotel();
                newhotel.setHName(hotel.getHName());
                newhotel.setHxLocation(hotel.getHxLocation());
                newhotel.setHyLocation(hotel.getHyLocation());
                hRepository.save(newhotel);
                map.put("result", "숙소등록완료");
            }
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

    // 여행지 조회(조건 : 지역 and 테마 일치)
    @PostMapping(value = "/select_td", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> tdSelect(@RequestBody TravelDestination td) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<TravelDestination> list = tdRepository.querySelectTD(td);
            map.put("status", 200);
            map.put("list", list);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

    // 여행지 조회(조건 : 지역 일치)
    @PostMapping(value = "select_hotel", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> hotelSelect(@RequestBody Hotel hotel) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<Hotel> list = hRepository.querySelectHotel(hotel);
            map.put("status", 200);
            map.put("list", list);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

    // 여행지 상세정보 (어떤식으로 표현할지 회의필요 > no 정보를 어떻게 받을지)
    @PostMapping(value = "/selectone", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object selectItemOneGET(@RequestBody long no) {
        Map<String, Object> map = new HashMap<>();
        try {
            TravelDestination td = tdRepository.findById(no).orElseThrow();
            map.put("status", 200);
            map.put("travelDestination", td);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

}
