package com.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.City;
import com.example.repository.CityRepository;
import com.example.repository.TDRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@RestController
@RequestMapping(value = "/travel")
public class TravelController {

    @Autowired
    TDRepository tdRepository;

    @Value("${tourapi.key}")
    private String tourApiKey;

    @Autowired
    CityRepository cityRepository;

    // 지역코드조회
    // 서울 1, 부산 6
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

                    if (areacode != null) {
                        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
                        for (int i = 0; i < j1.length(); i++) {
                            Map<String, Object> a = new HashMap<>();
                            if (!j1.getJSONObject(i).isNull("code"))
                                a.put("code", j1.getJSONObject(i).getInt("code"));
                            if (!j1.getJSONObject(i).isNull("name"))
                                a.put("name", j1.getJSONObject(i).getString("name"));

                            // City city = new City();

                            // if(!cityRepository.findById(j1.getJSONObject(i).getInt("code")).isPresent()){
                            // cityRepository.save(entity)
                            // }
                        }
                    }

                    // map.put("list", list1);
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
    // page , cnt = 검색수 , arrange = P (조회수순), O(제목순), contentTypeId= 폴더안에 엑셀파일 참조,
    // areacode= 서울1,부산6...
    @GetMapping(value = "/tourapi/select")
    public Map<String, Object> gettourapiselect(@RequestParam(name = "page", defaultValue = "1") String page,
            @RequestParam("cnt") String cnt, @RequestParam("arrange") String arrange,
            @RequestParam(name = "contentTypeId", defaultValue = "") String contentTypeId,
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

                    Integer totalCount = jsonObject.getJSONObject("response").getJSONObject("body")
                            .getInt("totalCount");
                    // System.out.println(j1.getJSONObject(1));
                    List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
                    for (int i = 0; i < j1.length(); i++) {
                        Map<String, Object> a = new HashMap<>();
                        if (!j1.getJSONObject(i).isNull("mapx") && !j1.getJSONObject(i).isNull("mapy")) {
                            if (!j1.getJSONObject(i).isNull("title"))
                                a.put("title", j1.getJSONObject(i).getString("title"));
                            if (!j1.getJSONObject(i).isNull("addr1"))
                                a.put("addr", j1.getJSONObject(i).getString("addr1"));
                            if (!j1.getJSONObject(i).isNull("areacode"))
                                a.put("areacode", j1.getJSONObject(i).getInt("areacode"));
                            if (!j1.getJSONObject(i).isNull("contentid"))
                                a.put("contentid", j1.getJSONObject(i).getInt("contentid"));
                            if (!j1.getJSONObject(i).isNull("firstimage"))
                                a.put("firstimage", j1.getJSONObject(i).getString("firstimage"));
                            a.put("mapx", j1.getJSONObject(i).getFloat("mapx"));
                            a.put("mapy", j1.getJSONObject(i).getFloat("mapy"));
                        }
                        list1.add(a);

                    }
                    // System.out.println(list1);
                    map.put("list", list1);
                    map.put("areatotal", totalCount);
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

    // tourapi 상세페이지
    // contentId
    @GetMapping(value = "/tourapi/selectone")
    public Map<String, Object> gettourapiselectone(@RequestParam("contentId") String contentId) {
        Map<String, Object> map = new HashMap<>();
        try {

            String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?serviceKey="
                    + tourApiKey + "&MobileOS=ETC&MobileApp=AppTest&contentId=" + contentId
                    + "&defaultYN=Y&firstImageYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&_type=json";

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

                    JSONObject j1 = jsonObject.getJSONObject("response").getJSONObject("body").getJSONObject("items")
                            .getJSONObject("item");

                    // System.out.println(j1.isNull("mayp"));
                    Map<String, Object> a = new HashMap<>();
                    if (!j1.isNull("mapx") && !j1.isNull("mapy")) {
                        if (!j1.isNull("title"))
                            a.put("title", j1.getString("title"));
                        if (!j1.isNull("addr1"))
                            a.put("addr", j1.getString("addr1"));
                        if (!j1.isNull("firstimage"))
                            a.put("firstimage", j1.getString("firstimage"));
                        if (!j1.isNull("firstimage2"))
                            a.put("firstimage2", j1.getString("firstimage2"));
                        if (!j1.isNull("overview"))
                            a.put("overview", j1.getString("overview"));
                        a.put("mapx", j1.getFloat("mapx"));
                        a.put("mapy", j1.getFloat("mapy"));
                    }
                    map.put("list", a);
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

    @GetMapping(value = "/distance")
    public Map<String, Object> locationDistance(@RequestParam("areaCode") String areaCode,
            @RequestParam("Cnt") String Cnt, @RequestParam("xmap") Float xmap, @RequestParam("pageNo") String pageNo,
            @RequestParam("ymap") Float ymap, @RequestParam("contentTypeId") String contentTypeId,
            @RequestParam(name = "kilometer", defaultValue = "15") double kilometer) {
        Map<String, Object> map = new HashMap<>();
        try {
            String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?serviceKey="
                    + tourApiKey + "&pageNo=" + pageNo + "&numOfRows=" + Cnt + "&contentTypeId=" + contentTypeId
                    + "&MobileApp=AppTest&MobileOS=ETC&arrange=P&areaCode=" + areaCode + "&listYN=Y&_type=json";

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
                    System.out.println(j1.getJSONObject(1).getString("addr1"));
                    List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
                    for (int i = 0; i < j1.length(); i++) {
                        Map<String, Object> a = new HashMap<>();
                        if (!j1.getJSONObject(i).isNull("mapx") && !j1.getJSONObject(i).isNull("mapy")) {
                            if (distance(ymap, xmap, j1.getJSONObject(i).getFloat("mapy"),
                                    j1.getJSONObject(i).getFloat("mapx"), "kilometer") < kilometer) {
                                if (!j1.getJSONObject(i).isNull("title"))
                                    a.put("title", j1.getJSONObject(i).getString("title"));
                                if (!j1.getJSONObject(i).isNull("addr1"))
                                    a.put("addr", j1.getJSONObject(i).getString("addr1"));
                                if (!j1.getJSONObject(i).isNull("areacode"))
                                    a.put("areacode", j1.getJSONObject(i).getInt("areacode"));
                                if (!j1.getJSONObject(i).isNull("contentid"))
                                    a.put("contentid", j1.getJSONObject(i).getInt("contentid"));
                                if (!j1.getJSONObject(i).isNull("firstimage"))
                                    a.put("firstimage", j1.getJSONObject(i).getString("firstimage"));
                                a.put("mapx", j1.getJSONObject(i).getFloat("mapx"));
                                a.put("mapy", j1.getJSONObject(i).getFloat("mapy"));
                                a.put("distanceKiloMeter", distance(ymap, xmap, j1.getJSONObject(i).getFloat("mapy"),
                                        j1.getJSONObject(i).getFloat("mapx"), "kilometer"));
                                list1.add(a);
                            }
                        }
                    }
                    System.out.println(list1.size());
                    // System.out.println(list1);
                    System.out.println(distance(ymap, xmap, j1.getJSONObject(1).getFloat("mapy"),
                            j1.getJSONObject(1).getFloat("mapx"), "kilometer"));
                    map.put("list", list1);
                }
            } else
                System.err.println("Error Occurred");

            map.put("status", 200);
            // System.out.println(map);
        } catch (

        Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;

    }

    // lat1 지점 1 위도
    // lon1 지점 1 경도
    // lat2 지점 2 위도
    // lon2 지점 2 경도
    // unit 거리 표출단위
    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if (unit == "meter") {
            dist = dist * 1609.344;
        }

        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
