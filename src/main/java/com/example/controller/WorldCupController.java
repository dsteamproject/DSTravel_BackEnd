package com.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.entity.City;
import com.example.entity.TD;
import com.example.entity.Type;
import com.example.entity.Worldcup;
import com.example.repository.CityRepository;
import com.example.repository.TDRepository;
import com.example.repository.TypeRepository;
import com.example.repository.WorldcupRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/worldcup")
public class WorldCupController {

    @Autowired
    TDRepository tdRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    WorldcupRepository worldcupRepository;

    @Autowired
    TypeRepository typeRepository;

    @GetMapping(value = "/home", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> homeGET(@RequestParam("city") Integer city, @RequestParam("type") Integer type) {
        Map<String, Object> map = new HashMap<>();
        try {
            City city2 = cityRepository.findById(city).get();
            Type type2 = typeRepository.findById(type).get();
            List<TD> list = tdRepository.selectTDWorldCup(city2, type2);
            map.put("listCnt", list.size());
            map.put("status", 200);

        } catch (Exception e) {
            map.put("status", e.hashCode());
        }
        return map;
    }

    @PostMapping(value = "/home", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> homePost(@RequestParam("city") Integer city, @RequestParam("type") Integer type,
            @RequestParam("size") Integer size) {
        Map<String, Object> map = new HashMap<>();
        try {
            City city2 = cityRepository.findById(city).get();
            Type type2 = typeRepository.findById(type).get();
            List<TD> list = tdRepository.selectTDWorldCup(city2, type2);
            List<TD> list1 = new ArrayList<>();
            List<TD> list2 = new ArrayList<>();
            int a[] = new int[size * 2];
            Random r = new Random();
            for (int i = 0; i < (size * 2); i++) {
                a[i] = r.nextInt(size * 2) + 1;
                for (int j = 0; j < i; j++) {
                    if (a[i] == a[j]) {
                        i--;
                    }
                }
            }
            for (int b = 0; b < size; b++) {
                list1.add(list.get(a[b] - 1));
            }
            for (int c = size; c < (size * 2); c++) {
                list2.add(list.get(a[c] - 1));
            }
            map.put("randomlist1", list1);
            map.put("randomlist2", list2);
            map.put("status", 200);

        } catch (Exception e) {
            map.put("status", e.hashCode());
        }
        return map;
    }

    // 월드컵 저장
    @PostMapping(value = "/save", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> savePOST(@RequestBody TD td) {
        Map<String, Object> map = new HashMap<>();
        try {
            Worldcup worldcup = new Worldcup();
            TD td1 = tdRepository.getById(td.getNo());
            worldcup.setTd(td1);
            worldcupRepository.save(worldcup);
            map.put("status", 200);
        } catch (Exception e) {
            map.put("status", e.hashCode());
        }
        return map;
    }

 
}
