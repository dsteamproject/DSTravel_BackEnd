package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.City;
import com.example.entity.Sigungu;
import com.example.entity.TD;
import com.example.entity.Type;
import com.example.repository.Cat1Repository;
import com.example.repository.Cat2Repository;
import com.example.repository.Cat3Repository;
import com.example.repository.CityRepository;
import com.example.repository.SigunguRepository;
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

// hsyu tour update test1
@RestController
public class TourApiController {

    @Autowired
    TDRepository tdRepository;

    @Value("${tourapi.key}")
    private String tourApiKey;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    SigunguRepository sigunguRepository;

    @Autowired
    Cat1Repository cat1Repository;

    @Autowired
    Cat2Repository cat2Repository;

    @Autowired
    Cat3Repository cat3Repository;

    @Autowired
    TypeRepository typeRepository;

    // 전체지역코드조회 [City , Sigungu DB저장용]
    @GetMapping(value = "/tourapi/DB/city_sigungu")
    public Map<String, Object> getTourApicityDB(@RequestParam(name = "cnt", defaultValue = "40") String cnt,
            @RequestParam(name = "areacode", defaultValue = "") String areacode) {
        Map<String, Object> map = new HashMap<>();
        try {

            String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaCode?serviceKey=" + tourApiKey
                    + "&numOfRows=" + cnt + "&MobileOS=ETC&MobileApp=AppTest&areaCode=" + areacode + "&_type=json";

            OkHttpClient client = new OkHttpClient();

            Request.Builder builder = new Request.Builder().url(url).get();
            Request request = builder.build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();

                if (body != null) {
                    String bodyString = body.string();

                    JSONObject jsonObject = new JSONObject(bodyString);

                    // System.out.println(jsonObject.getJSONObject("response").getJSONObject("body").getJSONObject("items")
                    // .getJSONObject("item"));
                    JSONObject j1 = jsonObject.getJSONObject("response").getJSONObject("body").getJSONObject("items");

                    if (areacode.equals("")) {
                        // List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
                        // Map<String, Object> a = new HashMap<>();
                        for (int i = 0; i < j1.getJSONArray("item").length(); i++) {
                            City city = new City();
                            if (!j1.getJSONArray("item").getJSONObject(i).isNull("code")) {
                                // a.put("code", j1.getJSONObject(i).getInt("code"));
                                city.setCode(j1.getJSONArray("item").getJSONObject(i).getInt("code"));
                            }
                            if (!j1.getJSONArray("item").getJSONObject(i).isNull("name")) {
                                // a.put("name", j1.getJSONObject(i).getString("name"));
                                city.setName(j1.getJSONArray("item").getJSONObject(i).getString("name"));
                            }
                            // if (a.size() != 0)
                            // list1.add(a);
                            if (!cityRepository.findById(j1.getJSONArray("item").getJSONObject(i).getInt("code"))
                                    .isPresent()) {
                                cityRepository.save(city);
                                // map.put("result", "DB에 저장 성공");
                                System.out.println(i + ":DB에 저장됨");
                            } else {
                                // map.put("result", "중복된 데이터");
                                System.out.println(i + ":DB에 존재하는 데이터");
                            }
                        }
                        // map.put("list", list1);
                    } else if (areacode.equals("8")) {
                        Sigungu sigungu = new Sigungu();
                        sigungu.setCity(cityRepository.findById(Integer.parseInt(areacode)).orElseThrow());
                        if (!j1.getJSONObject("item").isNull("code")) {
                            // a.put("code", j1.getJSONObject(i).getInt("code"));
                            sigungu.setCode(j1.getJSONObject("item").getInt("code"));
                        }
                        if (!j1.getJSONObject("item").isNull("name")) {
                            // a.put("name", j1.getJSONObject(i).getString("name"));
                            sigungu.setName(j1.getJSONObject("item").getString("name"));
                        }
                        // if (a.size() != 0)
                        // list1.add(a);
                        if (!sigunguRepository
                                .queryfindByCode(j1.getJSONObject("item").getInt("code"),
                                        cityRepository.findById(Integer.parseInt(areacode)).orElseThrow())
                                .isPresent()) {
                            System.err.println(sigungu.toString());
                            sigunguRepository.save(sigungu);
                            // map.put("result", "DB에 저장 성공");
                            System.out.println("DB에 저장됨");
                        } else {
                            // map.put("result", "중복된 데이터");
                            System.out.println("DB에 존재하는 데이터");
                        }

                    } else {
                        for (int i = 0; i < j1.getJSONArray("item").length(); i++) {
                            Sigungu sigungu = new Sigungu();
                            sigungu.setCity(cityRepository.findById(Integer.parseInt(areacode)).orElseThrow());
                            if (!j1.getJSONArray("item").getJSONObject(i).isNull("code")) {
                                // a.put("code", j1.getJSONObject(i).getInt("code"));
                                sigungu.setCode(j1.getJSONArray("item").getJSONObject(i).getInt("code"));
                            }
                            if (!j1.getJSONArray("item").getJSONObject(i).isNull("name")) {
                                // a.put("name", j1.getJSONObject(i).getString("name"));
                                sigungu.setName(j1.getJSONArray("item").getJSONObject(i).getString("name"));
                            }
                            // if (a.size() != 0)
                            // list1.add(a);
                            if (!sigunguRepository
                                    .queryfindByCode(j1.getJSONArray("item").getJSONObject(i).getInt("code"),
                                            cityRepository.findById(Integer.parseInt(areacode)).orElseThrow())
                                    .isPresent()) {
                                System.err.println(sigungu.toString());
                                sigunguRepository.save(sigungu);
                                // map.put("result", "DB에 저장 성공");
                                System.out.println(i + ":DB에 저장됨");
                            } else {
                                // map.put("result", "중복된 데이터");
                                System.out.println(i + ":DB에 존재하는 데이터");
                            }
                        }
                    }

                }
            } else {
                System.err.println("Error Occurred");
            }
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;

    }

    // type DB저장용
    // Body : code, name
    @PostMapping(value = "/tourapi/DB/type")
    public Map<String, Object> getTourApiTypeDB(@RequestBody List<Type> list) {
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

    // tourapi [국문] [TD DB저장용]
    // 반드시 city, (현재는 사용안함 : sigungu, cat1, cat2, cat3), type 자료를 DB에 저장 후 진행할것
    @GetMapping(value = "/tourapi/DB/TD")
    public Map<String, Object> getTourApiTDDB(@RequestParam(name = "page", defaultValue = "1") String page,
            @RequestParam("cnt") String cnt, @RequestParam(name = "arrange", defaultValue = "P") String arrange,
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

                    // Integer totalCount =
                    // jsonObject.getJSONObject("response").getJSONObject("body")
                    // .getInt("totalCount");
                    // System.out.println(j1.getJSONObject(i).getInt("contentid"));

                    // List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
                    for (int i = 0; i < j1.length(); i++) {
                        // Map<String, Object> a = new HashMap<>();
                        TD td = new TD();
                        if (!j1.getJSONObject(i).isNull("contentid"))
                            if (!tdRepository.findById(j1.getJSONObject(i).getInt("contentid")).isPresent()) {
                                if (!j1.getJSONObject(i).isNull("mapx") && !j1.getJSONObject(i).isNull("mapy")) {

                                    td.setCode(j1.getJSONObject(i).getInt("contentid"));
                                    // a.put("contentid", j1.getJSONObject(i).getInt("contentid"));
                                    if (!j1.getJSONObject(i).isNull("title"))
                                        td.setTitle(j1.getJSONObject(i).getString("title"));
                                    // a.put("title", j1.getJSONObject(i).getString("title"));
                                    if (!j1.getJSONObject(i).isNull("addr1"))
                                        td.setAddr(j1.getJSONObject(i).getString("addr1"));
                                    // a.put("addr", j1.getJSONObject(i).getString("addr1"));
                                    if (!j1.getJSONObject(i).isNull("areacode"))
                                        if (cityRepository.findById(j1.getJSONObject(i).getInt("areacode")).isPresent())
                                            td.setCity(cityRepository.findById(j1.getJSONObject(i).getInt("areacode"))
                                                    .orElseThrow());
                                    // a.put("areacode", j1.getJSONObject(i).getInt("areacode"));
                                    if (!j1.getJSONObject(i).isNull("firstimage"))
                                        td.setFirstimage(j1.getJSONObject(i).getString("firstimage"));
                                    if (!j1.getJSONObject(i).isNull("cat1"))
                                        if (cat1Repository.findById(j1.getJSONObject(i).getString("cat1")).isPresent())
                                            td.setCat1(cat1Repository.findById(j1.getJSONObject(i).getString("cat1"))
                                                    .orElseThrow());
                                    if (!j1.getJSONObject(i).isNull("cat2"))
                                        if (cat2Repository.findById(j1.getJSONObject(i).getString("cat2")).isPresent())
                                            td.setCat1(cat1Repository.findById(j1.getJSONObject(i).getString("cat2"))
                                                    .orElseThrow());
                                    if (!j1.getJSONObject(i).isNull("cat3"))
                                        if (cat3Repository.findById(j1.getJSONObject(i).getString("cat3")).isPresent())
                                            td.setCat1(cat1Repository.findById(j1.getJSONObject(i).getString("cat3"))
                                                    .orElseThrow());
                                    if (!j1.getJSONObject(i).isNull("sigungucode"))
                                        if (sigunguRepository.findById(j1.getJSONObject(i).getInt("sigungucode"))
                                                .isPresent())
                                            td.setSigungu(sigunguRepository
                                                    .findById(j1.getJSONObject(i).getInt("sigungucode")).orElseThrow());
                                    if (!j1.getJSONObject(i).isNull("tel"))
                                        td.setTel(j1.getJSONObject(i).getString("tel"));
                                    if (!j1.getJSONObject(i).isNull("contenttypeid"))
                                        if (typeRepository.findById(j1.getJSONObject(i).getInt("contenttypeid"))
                                                .isPresent())
                                            td.setType(
                                                    typeRepository.findById(j1.getJSONObject(i).getInt("contenttypeid"))
                                                            .orElseThrow());
                                    // a.put("firstimage", j1.getJSONObject(i).getString("firstimage"));
                                    td.setXlocaion(j1.getJSONObject(i).getFloat("mapx"));
                                    td.setYlocation(j1.getJSONObject(i).getFloat("mapy"));
                                    // a.put("mapx", j1.getJSONObject(i).getFloat("mapx"));
                                    // a.put("mapy", j1.getJSONObject(i).getFloat("mapy"));
                                }

                                System.out.println(j1.getJSONObject(i).getInt("contentid"));
                                System.out.println(i + ":저장완료");
                                tdRepository.save(td);
                            } else {
                                System.out.println(j1.getJSONObject(i).getInt("contentid"));
                                System.out.println(i + ":이미 저장된 데이터");
                            }

                        // list1.add(a);

                    }
                    // System.out.println(list1);
                    // map.put("list", list1);
                    // map.put("areatotal", totalCount);
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

}
