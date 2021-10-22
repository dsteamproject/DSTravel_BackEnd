package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.City;
import com.example.entity.TravelDestination;
import com.example.repository.TravelDestinationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    TravelDestinationRepository tdRepository;

    @GetMapping(value = "/home")
    public Map<String, Object> homeGET() {
        Map<String, Object> map = new HashMap<>();
        return map;
    }

    @PostMapping(value = "/home_Travel", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> homePOST(@RequestBody City city, @RequestBody String tema,
            @RequestBody String regdate) {
        Map<String, Object> map = new HashMap<>();
        try{
           List<TravelDestination> list = tdRepository.querySelectTD(city);
           map.put("status", 200);
           map.put("list", list);
        }catch(Exception e){
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

}
