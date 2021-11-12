package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/kakao")
public class kakaoController {

    // /oauth/authorize?client_id={REST_API_KEY}&redirect_uri={REDIRECT_URI}&response_type=code

    // @GetMapping(value="/oauth/authorize")
    // public String kakaoConnect(
    // @RequestParam(name = "REST_API_KEY") String rest_api_key,
    // @RequestParam(name = "REDORECT_URI") String redirect_uri,
    // @RequestParam(name = "response_type") String response_type
    // ) {

    // StringBuffer url = new StringBuffer();
    // url.append("https://kauth.kakao.com/oauth/authorize?");
    // url.append("client_id=" + "클라이언트에 등록된 client_id");
    // url.append("&redirect_uri=리다이렉트될 url");
    // url.append("&response_type=code");

    // return "redirect:" + url.toString();
    // }
}
