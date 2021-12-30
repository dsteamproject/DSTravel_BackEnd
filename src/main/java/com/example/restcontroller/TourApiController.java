package com.example.restcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.City;
import com.example.entity.TD;
import com.example.entity.TDType;
import com.example.repository.CityRepository;
import com.example.repository.TDRepository;
import com.example.repository.TypeRepository;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@RestController
public class TourApiController {

    @Autowired
    TDRepository tdRepository;

    @Value("${tourapi.key}")
    private String tourApiKey;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    TypeRepository typeRepository;

    // 전체지역코드조회 [City DB저장]
    // GET > http://localhost:8080/REST/tourapi/DB/city_sigungu
    @GetMapping(value = "/tourapi/DB/city_sigungu")
    public Map<String, Object> getTourApicityDB(@RequestParam(name = "cnt", defaultValue = "40") String cnt,
            @RequestParam(name = "areacode", defaultValue = "") String areacode) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 공공API_저역코드조회 URL
            String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaCode?serviceKey=" + tourApiKey
                    + "&numOfRows=" + cnt + "&MobileOS=ETC&MobileApp=AppTest&areaCode=" + areacode + "&_type=json";
            // OkHttp 클라이언트 객체 생성
            OkHttpClient client = new OkHttpClient();

            // GET 요청 객체 생성
            Request.Builder builder = new Request.Builder().url(url).get();
            Request request = builder.build();

            // OkHttp 클라이언트로 GET 요청 객체 전송
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // 응답 받아서 처리
                ResponseBody body = response.body();
                if (body != null) {
                    String bodyString = body.string();
                    // body 데이터를 String -> JSON 형태로 변환
                    JSONObject jsonObject = new JSONObject(bodyString);
                    // body 데이터 중 필요 데이터 수집
                    JSONObject j1 = jsonObject.getJSONObject("response").getJSONObject("body").getJSONObject("items");
                    if (areacode.equals("")) {
                        for (int i = 0; i < j1.getJSONArray("item").length(); i++) {
                            City city = new City();
                            if (!j1.getJSONArray("item").getJSONObject(i).isNull("code")) 
                                city.setCode(j1.getJSONArray("item").getJSONObject(i).getInt("code"));
                            
                            if (!j1.getJSONArray("item").getJSONObject(i).isNull("name")) 
                                city.setName(j1.getJSONArray("item").getJSONObject(i).getString("name"));
                            
                            if (!cityRepository.findById(j1.getJSONArray("item").getJSONObject(i).getInt("code"))
                                    .isPresent())
                                cityRepository.save(city);  
                        }
                    }
                }
            } else {
                System.err.println("Error Occurred");
            }
            map.put("status", 200);
            map.put("data", "전체지역코드 저장에 성공하였습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
            map.put("data", "전체지역코드 저장에 실패하였습니다.");
        }
        return map;
    }

    // type DB저장용
    // Body : code, name
    @PostMapping(value = "/tourapi/DB/type")
    public Map<String, Object> getTourApiTypeDB(@RequestBody List<TDType> list) {
        Map<String, Object> map = new HashMap<>();
        try {
            for (int i = 0; i < list.size(); i++) {
                typeRepository.save(list.get(i));
            }
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;

    }

    // 관광정보조회 [여행지 DB저장]
    // GET > http://localhost:8080/REST/tourapi/DB/TD
    @GetMapping(value = "/tourapi/DB/TD")
    public Map<String, Object> getTourApiTDDB(
        @RequestParam(name = "page", defaultValue = "1") String page,                   
        @RequestParam("cnt") String cnt,            
        @RequestParam(name = "arrange", defaultValue = "P") String arrange,
        @RequestParam(name = "contentTypeId", defaultValue = "") String contentTypeId,
        @RequestParam("areaCode") String areaCode) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 공공API_저역코드조회 URL
            String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?serviceKey="
                    + tourApiKey + "&pageNo=" + page + "&numOfRows=" + cnt + "&MobileApp=AppTest&MobileOS=ETC&arrange="
                    + arrange + "&contentTypeId=" + contentTypeId + "&areaCode=" + areaCode + "&listYN=Y&_type=json";
            
            // OkHttp 클라이언트 객체 생성
            OkHttpClient client = new OkHttpClient();

            // GET 요청 객체 생성
            Request.Builder builder = new Request.Builder().url(url).get();
            Request request = builder.build();

            // OkHttp 클라이언트로 GET 요청 객체 전송
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // 응답 받아서 처리
                ResponseBody body = response.body();

                if (body != null) {
                    String bodyString = body.string();
                    
                    // body 데이터를 String -> JSON 형태로 변환
                    JSONObject jsonObject = new JSONObject(bodyString);
                    
                    // body 데이터 중 필요 데이터 수집
                    JSONArray j1 = jsonObject.getJSONObject("response").getJSONObject("body").getJSONObject("items")
                            .getJSONArray("item");
                    for (int i = 0; i < j1.length(); i++) {
                        TD td = new TD();
                        if (!j1.getJSONObject(i).isNull("contentid")){
                            if (!tdRepository.findById(j1.getJSONObject(i).getInt("contentid")).isPresent()) {
                                if (!j1.getJSONObject(i).isNull("mapx") && !j1.getJSONObject(i).isNull("mapy")) {
                                    td.setXlocation(j1.getJSONObject(i).getFloat("mapx"));
                                    td.setYlocation(j1.getJSONObject(i).getFloat("mapy"));
                                    td.setCode(j1.getJSONObject(i).getInt("contentid"));
                                    if (!j1.getJSONObject(i).isNull("title"))
                                        td.setTitle(j1.getJSONObject(i).getString("title"));
                                    if (!j1.getJSONObject(i).isNull("addr1"))
                                        td.setAddr(j1.getJSONObject(i).getString("addr1"));
                                    if (!j1.getJSONObject(i).isNull("areacode"))
                                        if (cityRepository.findById(j1.getJSONObject(i).getInt("areacode")).isPresent())
                                            td.setCity(cityRepository.findById(j1.getJSONObject(i).getInt("areacode"))
                                                    .orElseThrow());
                                    if (!j1.getJSONObject(i).isNull("firstimage"))
                                        td.setFirstimage(j1.getJSONObject(i).getString("firstimage"));
                                    if (!j1.getJSONObject(i).isNull("tel"))
                                        td.setTel(j1.getJSONObject(i).getString("tel"));
                                    if (!j1.getJSONObject(i).isNull("contenttypeid"))
                                        if (typeRepository.findById(j1.getJSONObject(i).getInt("contenttypeid"))
                                                .isPresent())
                                            td.setTdtype(typeRepository.findById(j1.getJSONObject(i).getInt("contenttypeid")).orElseThrow());
                                }
                                tdRepository.save(td);
                            }
                        }
                    }
                }
            } else
                System.err.println("Error Occurred");

            map.put("status", 200);
            map.put("data", "관광정보 저장에 성공하였습니다.");
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("status", e.hashCode());
            map.put("data", "관광정보 저장에 실패하였습니다.");
        }
        // 결과 값 리턴
        return map;
    }
}
