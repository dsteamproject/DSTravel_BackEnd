package com.example.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;

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
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

// hsyu travel update
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

    // 127.0.0.1:8080/REST/travel/image1
    @GetMapping(value = "/image1", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType() throws IOException {
        InputStream in = getClass().getResourceAsStream("/static/image/1.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(value = "/image2", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType1() throws IOException {
        InputStream in = getClass().getResourceAsStream("/static/image/2.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(value = "/image3", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType2() throws IOException {
        InputStream in = getClass().getResourceAsStream("/static/image/3.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(value = "/image4", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType3() throws IOException {
        InputStream in = getClass().getResourceAsStream("/static/image/4.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(value = "/image5", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType4() throws IOException {
        InputStream in = getClass().getResourceAsStream("/static/image/5.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(value = "/image6", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType5() throws IOException {
        InputStream in = getClass().getResourceAsStream("/static/image/6.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(value = "/image7", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType6() throws IOException {
        InputStream in = getClass().getResourceAsStream("/static/image/7.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(value = "/image8", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType7() throws IOException {
        InputStream in = getClass().getResourceAsStream("/static/image/8.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(value = "/image9", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType8() throws IOException {
        InputStream in = getClass().getResourceAsStream("/static/image/9.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(value = "/image10", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType9() throws IOException {
        InputStream in = getClass().getResourceAsStream("/static/image/10.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(value = "/image11", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType10() throws IOException {
        InputStream in = getClass().getResourceAsStream("/static/image/11.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(value = "/image12", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType11() throws IOException {
        InputStream in = getClass().getResourceAsStream("/static/image/12.png");
        return IOUtils.toByteArray(in);
    }

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
            map.put("distanceList", list1);
            map.put("status", 200);

        } catch (

        Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;

    }

}