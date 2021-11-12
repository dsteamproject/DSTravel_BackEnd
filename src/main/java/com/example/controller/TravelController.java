package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.City;
import com.example.entity.Sigungu;
import com.example.entity.TD;
import com.example.repository.Cat1Repository;
import com.example.repository.Cat2Repository;
import com.example.repository.Cat3Repository;
import com.example.repository.CityRepository;
import com.example.repository.SigunguRepository;
import com.example.repository.TDRepository;
import com.example.repository.TypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/travel")
public class TravelController {

    @Autowired
    TDRepository tdRepository;

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

    // DB저장된 지역코드(City, Sigungu) 조회
    // Param : size(표시할 갯수) , page(페이지넘버) , areacode("" : City , "number" : Sigungu)
    // Return : Citylist or Sigungulist
    @GetMapping(value = "/city")
    public Map<String, Object> gettourapicity(
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "areacode", defaultValue = "") String areacode) {
        Map<String, Object> map = new HashMap<>();
        try {

            PageRequest pageRequest = PageRequest.of(page - 1, size);
            List<City> citylist = cityRepository.findAll(pageRequest).toList();
            if (areacode.equals(""))
                map.put("Citylist", citylist);
            else {
                List<Sigungu> sigungulist = sigunguRepository
                        .queryfindByCode(cityRepository.findById(Integer.parseInt(areacode)).orElseThrow());
                map.put("Sigungulist", sigungulist);
            }
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;

    }

    // 지역, contenttype별 목록
    // Param : areacode, contenttypeId, page
    @GetMapping(value = "/select")
    public Map<String, Object> getTourApiselect(@RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "title", defaultValue = "") String title,
            @RequestParam(name = "contentTypeId", defaultValue = "") String contentTypeId,
            @RequestParam("areaCode") String areaCode) {
        Map<String, Object> map = new HashMap<>();
        try {
            PageRequest pageRequest = PageRequest.of(page - 1, size);
            List<TD> list = tdRepository.querySelectTD(title, areaCode, contentTypeId, pageRequest);
            int cnt = tdRepository.CountSelectTD(title, areaCode, contentTypeId);
            System.out.println(cnt);
            map.put("cnt", (cnt - 1) / size + 1);
            map.put("list", list);
            map.put("status", 200);

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

    // tourapi 상세페이지
    // Param : contentId(Code)
    // Return : contentId에 해당하는 데이터
    @GetMapping(value = "/selectone")
    public Map<String, Object> gettourapiselectone(@RequestParam("contentId") String contentId) {
        Map<String, Object> map = new HashMap<>();
        try {
            TD td = tdRepository.querySelectOneTD(contentId);
            map.put("TD", td);
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;

    }

    @GetMapping(value = "/distance")
    public Map<String, Object> locationDistance(@RequestParam("areaCode") String areaCode,
            @RequestParam("xmap") Float xmap, @RequestParam("page") int page, @RequestParam("size") int size,
            @RequestParam("ymap") Float ymap, @RequestParam("contentTypeId") String contentTypeId,
            @RequestParam(name = "kilometer", defaultValue = "15") double kilometer) {
        Map<String, Object> map = new HashMap<>();
        try {
            PageRequest pageRequest = PageRequest.of(page - 1, size);
            List<TD> list1 = tdRepository.querySelectdistanceTD(areaCode, contentTypeId, xmap, ymap, kilometer,
                    pageRequest);

            int cnt = tdRepository.CountSelectdistanceTD(areaCode, contentTypeId, xmap, ymap, kilometer);
            System.out.println(cnt);
            map.put("cnt", (cnt - 1) / size + 1);
            map.put("distanceList", "반경" + kilometer + "km이내:" + list1);
            map.put("status", 200);

        } catch (

        Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;

    }
}