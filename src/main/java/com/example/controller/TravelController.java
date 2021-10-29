package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.Hotel;
import com.example.entity.TravelDestination;
import com.example.repository.HotelRepository;
import com.example.repository.TravelDestinationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/travel")
public class TravelController {

    @Autowired
    TravelDestinationRepository tdRepository;

    @Autowired
    HotelRepository hRepository;

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
