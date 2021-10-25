package com.example.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.example.entity.Member;
import com.example.jwt.JwtUtil;
import com.example.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/member")
public class MemberController {

    @Autowired
    MemberRepository mRepository;

    @Autowired
    JwtUtil jwtUtil;

    @GetMapping(value = "/join")
    public Map<String, Object> memberJoinGET() {
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("status", 200);
        } catch (Exception e) {
            map.put("status", e.hashCode());
        }
        return map;

    }

    @PostMapping(value = "/join", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> memberJoinPOST(@RequestBody Member member) {
        Map<String, Object> map = new HashMap<>();
        try {
            BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
            member.setPassword(bcpe.encode(member.getPassword()));
            mRepository.save(member);
            map.put("status", 200);
        } catch (Exception e) {
            map.put("status", e.hashCode());
        }
        return map;
    }

    @PostMapping(value = "/idcheck", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> idCheckPOST(@RequestBody Member id) {
        Map<String, Object> map = new HashMap<>();
        try {
            Optional<Member> member = mRepository.findById(id.getId());
            if (member.isPresent()) {
                map.put("result", "중복된 아이디 입니다.");
            } else {
                map.put("status", 200);
                map.put("result", "사용가능한 아이디 입니다.");
            }

        } catch (

        Exception e) {
            map.put("status", id.getId());
        }
        return map;
    }

    @GetMapping(value = "/login")
    public Map<String, Object> memberLoginGET() {
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("status", 200);
        } catch (Exception e) {
            map.put("status", e.hashCode());
        }
        return map;
    }

    @PostMapping(value = "/login", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> memberSelect(@RequestBody Member member) {
        Map<String, Object> map = new HashMap<>();
        try {
            BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
            if (mRepository.findById(member.getId()).isPresent()) {
                if (bcpe.matches(member.getPassword(), mRepository.getById(member.getId()).getPassword())) {
                    map.put("status", 200);
                    map.put("token", jwtUtil.generateToken(member.getId()).startsWith("rhznjfflxl"));
                } else {
                    map.put("result", "암호가 틀립니다.");
                }
            } else {
                map.put("result", "아이디가 틀립니다.");
            }
        } catch (Exception e) {
            map.put("status", e.hashCode());
        }
        return map;
    }

}
