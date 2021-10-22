package com.example.controller;

import java.util.HashMap;
import java.util.Map;

import com.example.entity.TravelDestination;
import com.example.repository.TravelDestinationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/travel")
public class TravelController {
    
    @Autowired
    TravelDestinationRepository tdRepository;

    @PostMapping(value = "/insert")
    public Map<String,Object> tdInsertPOST(@RequestBody TravelDestination td){
        Map<String,Object> map = new HashMap<>();
        try{
            tdRepository.save(td);

            map.put("status", 200);
        }catch(Exception e){
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }
}
