package com.example.service;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class RestJsonService {
    private final String GRANT_TYPE = "authorization_code";
    private final String KAKAO_CLIENT_ID = "ce61cbedf2c1d5758c73ec734dc1af08";
    private final String KAKAO_REDIRECT_URI = "http://localhost:8080/login/oauth2/code/kakao";
    private final String KAKAO_CLIENT_SECRET = "Qg7KSM8Cj7NGqt0UxJwXQQHJi8uETVLv";
    private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String NAVER_CLIENT_ID = "WCxe47guG90KEPP6lIWu";
    private final String NAVER_REDIRECT_URI = "http://localhost:8080/login/oauth2/code/naver";
    private final String NAVER_CLIENT_SECRET = "xLn4x5ySzK";
    private final String NAVER_TOKEN_URL = "https://nid.naver.com/oauth2.0/token";
    private final String GOOGLE_CLIENT_ID = "74537161972-j2r3otdejq6fs24eeo09tlpsm5lho3u0.apps.googleusercontent.com";
    private final String GOOGLE_REDIRECT_URI = "http://localhost:8080/login/oauth2/code/google";
    private final String GOOGLE_CLIENT_SECRET = "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";
    private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";

    public String getAccessTokenJsonDataKakao(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity request = new HttpEntity(headers);

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_TOKEN_URL)
                .queryParam("grant_type", GRANT_TYPE).queryParam("client_id", KAKAO_CLIENT_ID)
                .queryParam("redirect_uri", KAKAO_REDIRECT_URI).queryParam("code", code)
                .queryParam("client_secret", KAKAO_CLIENT_SECRET);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uriComponentsBuilder.toUriString(),
                HttpMethod.POST, request, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        }
        return "error";
    }

    public String getAccessTokenJsonDataNaver(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity request = new HttpEntity(headers);

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(NAVER_TOKEN_URL)
                .queryParam("grant_type", GRANT_TYPE).queryParam("client_id", NAVER_CLIENT_ID)
                .queryParam("redirect_uri", NAVER_REDIRECT_URI).queryParam("code", code)
                .queryParam("client_secret", NAVER_CLIENT_SECRET);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uriComponentsBuilder.toUriString(),
                HttpMethod.POST, request, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        }
        return "error";
    }

    public String getAccessTokenJsonDataGoogle(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity request = new HttpEntity(headers);

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(GOOGLE_TOKEN_URL)
                .queryParam("grant_type", GRANT_TYPE).queryParam("client_id", GOOGLE_CLIENT_ID)
                .queryParam("redirect_uri", GOOGLE_REDIRECT_URI).queryParam("code", code)
                .queryParam("client_secret", GOOGLE_CLIENT_SECRET);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uriComponentsBuilder.toUriString(),
                HttpMethod.POST, request, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        }
        return "error";
    }
}
