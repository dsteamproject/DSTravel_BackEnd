package com.example.service;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class RestJsonService {
    private final String GRANT_TYPE= "authorization_code";
    private final String CLIENT_ID = "ce61cbedf2c1d5758c73ec734dc1af08";
    private final String REDIRECT_URI= "http://localhost:8080/login/oauth2/code/kakao";
    private final String CLIENT_SECRET= "Qg7KSM8Cj7NGqt0UxJwXQQHJi8uETVLv";
    private final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";

    public String getAccessTokenJsonData(String code){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity request = new HttpEntity(headers);

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(TOKEN_URL)
                .queryParam("grant_type", GRANT_TYPE)
                .queryParam("client_id", CLIENT_ID)
                .queryParam("redirect_uri", REDIRECT_URI)
                .queryParam("code", code)
                .queryParam("client_secret", CLIENT_SECRET);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.POST,
                request,
                String.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        }
        return "error";
    }
}
