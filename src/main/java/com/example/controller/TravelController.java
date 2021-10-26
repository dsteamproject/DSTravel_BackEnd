package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.TravelDestination;
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

    // 미구현 장소 추가 기능
    @PostMapping(value = "/insert", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> tdInsert(@RequestBody TravelDestination td) {
        Map<String, Object> map = new HashMap<>();
        try {
            TravelDestination newtd = new TravelDestination();
            newtd.setTTitle(td.getTTitle());
            newtd.setTxLocation(td.getTxLocation());
            newtd.setTyLocation(td.getTyLocation());
            tdRepository.save(newtd);

            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

    // 해당 지역에 대한 여행지 조회
    @PostMapping(value = "/select", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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

}
