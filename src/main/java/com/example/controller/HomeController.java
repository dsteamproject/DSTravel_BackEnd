package com.example.controller;

import java.util.HashMap;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// import com.example.listener.HttpSessionCheckingListener;
import com.example.repository.TravelDestinationRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    TravelDestinationRepository tdRepository;

    // @Autowired
    // HttpSessionCheckingListener listener;

    @GetMapping(value = "/home")
    public Map<String, Object> homeGET(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> map = new HashMap<>();
        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession(true);
        }
        map.put("test", "Session Test completed");
        return map;
    }

    // @PostMapping(value = "/home_Travel", consumes = MediaType.ALL_VALUE, produces
    // = MediaType.APPLICATION_JSON_VALUE)
    // public Map<String, Object> homePOST(@RequestBody TravelDestination TD,
    // @RequestBody String regdate) {
    // Map<String, Object> map = new HashMap<>();
    // try {
    // List<TravelDestination> list = tdRepository.querySelectTD(TD);
    // map.put("status", 200);
    // map.put("list", list);
    // map.put("regdate", regdate);
    // } catch (Exception e) {
    // e.printStackTrace();
    // map.put("status", e.hashCode());
    // }
    // return map;
    // }

}
