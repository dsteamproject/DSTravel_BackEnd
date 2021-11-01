package com.example.controller;

import java.util.HashMap;
import java.util.Map;

import com.example.jwt.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping(value = "/mypage", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> mypageGet(@RequestHeader("TOKEN") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (jwtUtil.validateToken(token.substring(6), jwtUtil.extractUsername(token.substring(6)))) {

                // Member member =
                // mRepository.findById(jwtUtil.extractUsername(token.substring(6))).orElseThrow();
                // map.put("member", member);
                map.put("status", 200);

            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;

    }

}
