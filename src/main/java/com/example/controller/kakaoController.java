package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/login/oauth2/code")
public class KakaoController {

    @GetMapping(value = "/kakao")
    public String kakaoOauthRedirect(@RequestParam String code) {
        return "카카오 로그인 인증 완료, code : " + code;
    }

}
