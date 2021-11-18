package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.City;
import com.example.entity.TD;
import com.example.repository.TDRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/hotel")
public class HotelController {

    @Autowired
    TDRepository tdRepository;

    // 지역별, 가격별, 등급별 숙소 조회
    // price city rank
    @GetMapping(value = "/select")
    public Map<String, Object> getTourApiselect(@RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size, @RequestParam(name = "price") int price,
            @RequestParam(name = "city") Integer city, @RequestParam("rank") int rank) {
        Map<String, Object> map = new HashMap<>();
        try {
            PageRequest pageRequest = PageRequest.of(page - 1, size);
            List<TD> list = tdRepository.SelectHOTEL(price, city, rank, pageRequest);
            int cnt = tdRepository.COUNTSelectHOTEL(price, city, rank);
            // System.out.println(cnt);
            map.put("cnt", (cnt - 1) / size + 1);
            map.put("list", list);
            map.put("status", 200);

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

}
