package com.wanchcoach.domain.auth.apiClient;

import com.wanchcoach.domain.auth.application.OAuthProvider;
import com.wanchcoach.domain.auth.infoResponse.KakaoInfoResponse;
import com.wanchcoach.domain.auth.infoResponse.NaverInfoResponse;
import com.wanchcoach.domain.auth.infoResponse.OAuthInfoResponse;
import com.wanchcoach.domain.auth.params.OAuthLoginParams;

import com.wanchcoach.domain.auth.tokens.KakaoTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthApiClient{

    private final RestTemplate restTemplate;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String requestAccessToken(OAuthLoginParams params) {
        String url = "https://kauth.kakao.com/oauth/token";
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = params.makeBody();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "5a4189cd1aa8daccb6849a1046a81cc7");
        body.add("redirect_uri", "http://localhost:8081/login/oauth2/code/kakao");

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        KakaoTokens response = restTemplate.postForObject(url, request, KakaoTokens.class);
        System.out.println(response.toString());
        assert response != null;
        return response.getAccessToken();
    }

    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
        OAuthInfoResponse response = restTemplate.postForObject(url, request, KakaoInfoResponse.class);
        System.out.println(response.toString());
        return response;
    }
}
