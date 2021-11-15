package com.example.controller;

import java.util.HashMap;

import java.util.Map;

import com.example.service.GetUserInfoService;
import com.example.service.RestJsonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.repository.TDRepository;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    TDRepository tdRepository;

    @GetMapping(value = "/home")
    public Map<String, Object> homeGET(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> map = new HashMap<>();
        return map;
    }

    @GetMapping("/receiveAC")
    public Map<String,Object> receiveAC(@RequestParam("code") String code) {
        Map<String,Object> map = new HashMap<>();
        RestJsonService restJsonService = new RestJsonService();

        //access_token이 포함된 JSON String을 받아온다.
        String accessTokenJsonData = restJsonService.getAccessTokenJsonData(code);
        if(accessTokenJsonData=="error"){
            map.put("status", "error");
            return map;
        }

        //JSON String -> JSON Object
        JSONObject accessTokenJsonObject = new JSONObject(accessTokenJsonData);

        //access_token 추출
        String accessToken = accessTokenJsonObject.get("access_token").toString();
        // System.out.println(accessToken);


        //유저 정보가 포함된 JSON String을 받아온다.
        GetUserInfoService getUserInfoService = new GetUserInfoService();
        String userInfo = getUserInfoService.getUserInfo(accessToken);

        System.out.println("--------------"+userInfo);

        //JSON String -> JSON Object
        JSONObject userInfoJsonObject = new JSONObject(userInfo);

        //유저의 Email 추출
        // JSONObject propertiesJsonObject = (JSONObject)userInfoJsonObject.get("properties");
        // String profileImage = propertiesJsonObject.get("profile_image").toString();
        
        // System.out.println("--------------"+profileImage);

        JSONObject kakaoAccountJsonObject = (JSONObject)userInfoJsonObject.get("kakao_account");

        String email = kakaoAccountJsonObject.get("email").toString();
        // try{
        //     email = kakaoAccountJsonObject.get("email").toString();
            System.out.println("--------------"+email);
        // }
        // catch (Exception e){
        //     email = "약관 동의 안함";
        // }

        // map.put("profile_image", profileImage);
        map.put("email", email);

        return map;
    }
}
