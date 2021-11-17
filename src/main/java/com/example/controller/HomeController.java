package com.example.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import java.util.Map;

import com.example.entity.Member;
import com.example.entity.MemberImg;
import com.example.jwt.JwtUtil;
import com.example.repository.MemberImgRepository;
import com.example.repository.MemberRepository;
import com.example.service.GetUserInfoService;
import com.example.service.RestJsonService;

import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

    // https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=ce61cbedf2c1d5758c73ec734dc1af08&redirect_uri=http://localhost:8080/login/oauth2/code/kakao
    @PostMapping("/kakaologin")
    public Map<String, Object> kakaologin(@RequestParam("code") String code) {
        Map<String, Object> map = new HashMap<>();
        RestJsonService restJsonService = new RestJsonService();
        try {

            // access_token이 포함된 JSON String을 받아온다.
            String accessTokenJsonData = restJsonService.getAccessTokenJsonDataKakao(code);
            if (accessTokenJsonData == "error") {
                map.put("status", "error");
                return map;
            }
            System.out.println(accessTokenJsonData);

            // JSON String -> JSON Object
            JSONObject accessTokenJsonObject = new JSONObject(accessTokenJsonData);

            // access_token 추출
            String accessToken = accessTokenJsonObject.get("access_token").toString();
            System.out.println(accessToken);

            // 유저 정보가 포함된 JSON String을 받아온다.
            GetUserInfoService getUserInfoService = new GetUserInfoService();
            String userInfo = getUserInfoService.getUserInfoKakao(accessToken);

            System.out.println("--------------" + userInfo);

            // JSON String -> JSON Object
            JSONObject userInfoJsonObject = new JSONObject(userInfo);

            // 유저의 Email 추출

            JSONObject kakaoAccountJsonObject = (JSONObject) userInfoJsonObject.get("kakao_account");
            String id = userInfoJsonObject.get("id").toString();

            String email;
            String profile_image_url;
            try {
                profile_image_url = kakaoAccountJsonObject.getJSONObject("profile").getString("profile_image_url")
                        .toString();
                email = kakaoAccountJsonObject.get("email").toString();

            } catch (Exception e) {
                email = "";
                profile_image_url = "";
            }

            String token = jwtUtil.generateToken(id);
            Member member = new Member();
            MemberImg mImg = new MemberImg();
            if (!mRepository.findById(id).isPresent()) {
                BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
                member.setPassword(bcpe.encode("kakao"));
                member.setId(id);
                member.setLogin("SNS");
                member.setToken(token);
                member.setEmail(email);
                member.setNicname("익명");
                mRepository.save(member);

                URL u = new URL(profile_image_url);
                InputStream is = null;
                is = u.openStream();
                byte[] imageBytes = IOUtils.toByteArray(is);
                ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
                String mimeType = URLConnection.guessContentTypeFromStream(bais);
                mImg.setImage(imageBytes);
                mImg.setImagetype(mimeType);
                mImg.setMember(member);
                mImgRepository.save(mImg);
            } else {
                Member member1 = mRepository.findById(id).orElseThrow();
                member1.setToken(token);
                mRepository.save(member1);
            }
            map.put("status", 200);
            map.put("token", tokenwithvalue + token);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }

        return map;
    }

    // https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=WCxe47guG90KEPP6lIWu&redirect_uri=http://localhost:8080/login/oauth2/code/naver
    @PostMapping("/naverlogin")
    public Map<String, Object> naverlogin(@RequestParam("code") String code) {
        Map<String, Object> map = new HashMap<>();
        RestJsonService restJsonService = new RestJsonService();
        try {

            // access_token이 포함된 JSON String을 받아온다.
            String accessTokenJsonData = restJsonService.getAccessTokenJsonDataNaver(code);
            if (accessTokenJsonData == "error") {
                map.put("status", "error");
                return map;
            }

            // JSON String -> JSON Object
            JSONObject accessTokenJsonObject = new JSONObject(accessTokenJsonData);

            // access_token 추출
            String accessToken = accessTokenJsonObject.get("access_token").toString();
            System.out.println("1.--------------------" + accessToken);

            // 유저 정보가 포함된 JSON String을 받아온다.
            GetUserInfoService getUserInfoService = new GetUserInfoService();
            String userInfo = getUserInfoService.getUserInfoNaver(accessToken);

            System.out.println("2.--------------" + userInfo);

            // JSON String -> JSON Object
            JSONObject userInfoJsonObject = new JSONObject(userInfo);

            // 유저의 Email 추출

            JSONObject naverAccountJsonObject = (JSONObject) userInfoJsonObject.get("response");
            String id = naverAccountJsonObject.get("id").toString();
            String email;

            try {
                email = naverAccountJsonObject.getString("email").toString();
            } catch (Exception e) {
                email = "약관 동의 안함";
            }

            String token = jwtUtil.generateToken(id);
            Member member = new Member();

            if (!mRepository.findById(id).isPresent()) {
                BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
                member.setPassword(bcpe.encode("naver"));
                member.setLogin("SNS");
                member.setId(id);
                member.setNicname("익명");
                member.setToken(token);
                member.setEmail(email);
                mRepository.save(member);
            } else {
                Member member1 = mRepository.findById(id).orElseThrow();
                member1.setToken(token);
                mRepository.save(member1);
            }
            map.put("status", 200);
            map.put("token", tokenwithvalue + token);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }

        return map;
    }

    // https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=74537161972-j2r3otdejq6fs24eeo09tlpsm5lho3u0.apps.googleusercontent.com&redirect_uri=http://localhost:8080/login/oauth2/code/google&scope=email%20profile%20openid
    @PostMapping("/googlelogin")
    public Map<String, Object> googlelogin(@RequestParam("code") String code) {
        Map<String, Object> map = new HashMap<>();
        RestJsonService restJsonService = new RestJsonService();
        try {

            // access_token이 포함된 JSON String을 받아온다.
            String accessTokenJsonData = restJsonService.getAccessTokenJsonDataGoogle(code);
            if (accessTokenJsonData == "error") {
                map.put("status", "error");
                return map;
            }

            // JSON String -> JSON Object
            JSONObject accessTokenJsonObject = new JSONObject(accessTokenJsonData);

            // access_token 추출
            String accessToken = accessTokenJsonObject.get("access_token").toString();
            System.out.println("1.--------------------" + accessToken);

            // 유저 정보가 포함된 JSON String을 받아온다.
            GetUserInfoService getUserInfoService = new GetUserInfoService();
            System.out.println("2.--------------^^^^^^^^^^^^^^^^^^^^^^^^^^");
            String userInfo = getUserInfoService.getUserInfoGoogle(accessToken);

            System.out.println("2.--------------" + userInfo);

            // JSON String -> JSON Object
            JSONObject userInfoJsonObject = new JSONObject(userInfo);

            // 유저의 Email 추출
            String id = userInfoJsonObject.get("id").toString();
            String email;

            try {
                email = userInfoJsonObject.getString("email").toString();
            } catch (Exception e) {
                email = "약관 동의 안함";
            }

            String token = jwtUtil.generateToken(id);
            Member member = new Member();

            if (!mRepository.findById(id).isPresent()) {
                BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
                member.setPassword(bcpe.encode("google"));
                member.setLogin("SNS");
                member.setId(id);
                member.setNicname("익명");
                member.setToken(token);
                member.setEmail(email);
                mRepository.save(member);
            } else {
                Member member1 = mRepository.findById(id).orElseThrow();
                member1.setToken(token);
                mRepository.save(member1);
            }
            map.put("status", 200);
            map.put("token", tokenwithvalue + token);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }

        return map;
    }

    // @PostMapping("/googlelogout")
    // public Map<String, Object> googlelogout(@RequestParam("code") String code) {
    // Map<String, Object> map = new HashMap<>();
    // RestJsonService restJsonService = new RestJsonService();
    // try {

    // // access_token이 포함된 JSON String을 받아온다.
    // String accessTokenJsonData =
    // restJsonService.getAccessTokenJsonDataGoogle(code);
    // if (accessTokenJsonData == "error") {
    // map.put("status", "error");
    // return map;
    // }

    // // JSON String -> JSON Object
    // JSONObject accessTokenJsonObject = new JSONObject(accessTokenJsonData);

    // // access_token 추출
    // String accessToken = accessTokenJsonObject.get("access_token").toString();

    // RestTemplate restTemplate = new RestTemplate();

    // HttpHeaders headers = new HttpHeaders();
    // headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    // HttpEntity request = new HttpEntity(headers);

    // UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
    // .fromHttpUrl("https://oauth2.googleapis.com/revoke?token=" + accessToken);

    // ResponseEntity<String> responseEntity =
    // restTemplate.exchange(uriComponentsBuilder.toUriString(),
    // HttpMethod.POST, request, String.class);

    // if (responseEntity.getStatusCode() == HttpStatus.OK) {
    // map.put("status", 200);
    // } else {
    // map.put("status", "error");
    // return map;
    // }

    // } catch (Exception e) {
    // e.printStackTrace();
    // map.put("status", e.hashCode());
    // }

    // return map;
    // }

}
