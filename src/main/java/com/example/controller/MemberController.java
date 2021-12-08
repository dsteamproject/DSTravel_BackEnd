package com.example.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.example.entity.Member;
import com.example.jwt.JwtUtil;
import com.example.repository.MemberRepository;
import com.example.service.SendEmailService;
import com.example.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/member")
public class MemberController {

    @Value("${kakao.client_key}")
    private String kakaoClientKey;

    @Value("${kakao.redirect_key}")
    private String kakaoRedirectKey;

    @Autowired
    MemberRepository mRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    @Autowired
    SendEmailService sendEmailService;

    @Value("${tokenscretstart}")
    private String tokenwithvalue;

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
            map.put("test", e.getStackTrace());
        }
        return map;
    }

    @PostMapping(value = "/idcheck", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> idCheckPOST(@RequestBody Member id) {
        Map<String, Object> map = new HashMap<>();
        try {
            Optional<Member> member = mRepository.findById(id.getId());
            if (member.isPresent()) {
                map.put("status", 300);
                map.put("result", "중복된 아이디 입니다.");
            } else {
                map.put("status", 200);
                map.put("result", "사용가능한 아이디 입니다.");
            }

        } catch (Exception e) {
            map.put("status", e.hashCode());
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
            if (mRepository.querySelectByid(member.getId()).isPresent()) {
                if (bcpe.matches(member.getPassword(), mRepository.getById(member.getId()).getPassword())) {
                    String token = jwtUtil.generateToken(member.getId());
                    Member member1 = mRepository.getById(member.getId());
                    member1.setToken(token);
                    mRepository.save(member1);
                    map.put("status", 200);
                    map.put("token", tokenwithvalue + token);

                } else {
                    map.put("result", "암호가 틀립니다.");
                }
            } else if (mRepository.querySelectmember(member.getId()).isPresent()) {
                map.put("result", "탈퇴한 아이디 입니다.");

            } else {
                map.put("result", "아이디가 틀립니다.");
            }
        } catch (Exception e) {
            map.put("status", e.hashCode());
        }
        return map;
    }

    // 등록된 이메일로 임시비밀번호를 발송하고 발송된 임시비밀번호로 사용자의 pw를 변경하는 컨트롤러
    @PostMapping(value = "/check/findPw/sendEmail", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> sendEmail(@RequestBody Member member) {
        Map<String, Object> map = new HashMap<>();
        try {
            boolean pwFindCheck = userService.userEmailCheck(member.getEmail(), member.getName());
            if (pwFindCheck) {
                sendEmailService.sendEmailTempPassword(member.getEmail(), member.getName());
                map.put("status", 200);
            } else {
                map.put("status", 303);
                map.put("pwFindCheck", "가입한 이메일과 이름이 틀립니다.");
            }
        } catch (Exception e) {
            map.put("status", e.hashCode());
            e.printStackTrace();
        }
        return map;
    }

    @GetMapping(value = "/mypage", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> mypageGet(@RequestParam(name = "menu", defaultValue = "1") int menu,
            @RequestHeader("TOKEN") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            String id = jwtUtil.extractUsername(token.substring(6));
            Member member = mRepository.findById(id).orElseThrow();
            if (member != null && member.getToken().equals(token.substring(6))
                    && !jwtUtil.isTokenExpired(token.substring(6))) {
                if (menu == 1) {
                    map.put("member", member);
                    map.put("status", 200);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;

    }

}