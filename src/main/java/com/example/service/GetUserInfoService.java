package com.example.service;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class GetUserInfoService {
    private final String KAKAO_HTTP_REQUEST = "https://kapi.kakao.com/v2/user/me";
    private final String NAVER_HTTP_REQUEST = "https://openapi.naver.com/v1/nid/me";
    private final String GOOGLE_HTTP_REQUEST = "https://www.googleapis.com/oauth2/v1/userinfo";

    public String getUserInfoKakao(String accessToken) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity request = new HttpEntity(headers);

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_HTTP_REQUEST);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uriComponentsBuilder.toUriString(),
                HttpMethod.POST, request, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        }
        return "error";
    }

    public String getUserInfoNaver(String accessToken) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity request = new HttpEntity(headers);

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(NAVER_HTTP_REQUEST);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uriComponentsBuilder.toUriString(),
                HttpMethod.POST, request, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        }
        return "error";
    }

    public String getUserInfoGoogle(String accessToken) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity request = new HttpEntity(headers);

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(GOOGLE_HTTP_REQUEST);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uriComponentsBuilder.toUriString(),
                HttpMethod.GET, request, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        }
        return "error";
    }
}
