package com.example.controller;

import java.util.HashMap;

import java.util.Map;

import com.example.entity.Member;
import com.example.entity.MemberImg;
import com.example.jwt.JwtUtil;
import com.example.repository.MemberImgRepository;
import com.example.repository.MemberRepository;
import com.example.service.GetUserInfoService;
import com.example.service.RestJsonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    MemberRepository mRepository;

    @Autowired
    MemberImgRepository mImgRepository;

    @Value("${tokenscretstart}")
    private String tokenwithvalue;

    @GetMapping(value = "/home")
    public Map<String, Object> homeGET(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> map = new HashMap<>();
        return map;
    }

    @PostMapping("/kakaologin")
    public Map<String, Object> receiveAC(@RequestParam("code") String code) {
        Map<String, Object> map = new HashMap<>();
        RestJsonService restJsonService = new RestJsonService();
        try {

            // access_token이 포함된 JSON String을 받아온다.
            String accessTokenJsonData = restJsonService.getAccessTokenJsonData(code);
            if (accessTokenJsonData == "error") {
                map.put("status", "error");
                return map;
            }

            // JSON String -> JSON Object
            JSONObject accessTokenJsonObject = new JSONObject(accessTokenJsonData);

            // access_token 추출
            String accessToken = accessTokenJsonObject.get("access_token").toString();
            // System.out.println(accessToken);

            // 유저 정보가 포함된 JSON String을 받아온다.
            GetUserInfoService getUserInfoService = new GetUserInfoService();
            String userInfo = getUserInfoService.getUserInfo(accessToken);

            System.out.println("--------------" + userInfo);

            // JSON String -> JSON Object
            JSONObject userInfoJsonObject = new JSONObject(userInfo);

            // 유저의 Email 추출

            JSONObject kakaoAccountJsonObject = (JSONObject) userInfoJsonObject.get("kakao_account");
            String id = userInfoJsonObject.get("id").toString();

            String nickname;
            String email;
            String profile_image_url;
            try {
                nickname = kakaoAccountJsonObject.getJSONObject("profile").getString("nickname").toString();
                profile_image_url = kakaoAccountJsonObject.getJSONObject("profile").getString("profile_image_url")
                        .toString();
                email = kakaoAccountJsonObject.get("email").toString();

            } catch (Exception e) {
                email = "약관 동의 안함";
                nickname = "약관 동의 안함";
                profile_image_url = "약관 동의 안함";
            }

            String token = jwtUtil.generateToken(id);
            Member member = new Member();
            MemberImg mImg = new MemberImg();
            if (!mRepository.findById(id).isPresent()) {
                member.setId(id);
                if (!email.equals("약관 동의 안함"))
                    member.setEmail(email);
                if (!nickname.equals("약관 동의 안함"))
                    member.setNicname(nickname);
                // if(!profile_image_url.equals("약관 동의 안함"))

            }

            member.setToken(token);
            mRepository.save(member);

            map.put("status", 200);
            map.put("token", tokenwithvalue + token);
            map.put("profile_image_url", profile_image_url);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }

        return map;
    }
}
