package com.wanchcoach.domain.auth.apiClient;

import com.wanchcoach.domain.auth.application.OAuthProvider;
import com.wanchcoach.domain.auth.infoResponse.GoogleInfoResponse;
import com.wanchcoach.domain.auth.infoResponse.NaverInfoResponse;
import com.wanchcoach.domain.auth.infoResponse.OAuthInfoResponse;
import com.wanchcoach.domain.auth.params.GoogleLoginParams;
import com.wanchcoach.domain.auth.params.OAuthLoginParams;
import com.wanchcoach.domain.auth.tokens.GoogleTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GoogleApiClient implements OAuthApiClient{

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.google.url.auth}")
    private String authUrl;


    @Value("${oauth.google.client_id}")
    private String clientId;

    @Value("${oauth.google.secret}")
    private String clientSecret;

    @Value("${oauth.google.redirect_uri}")
    private String redirectUri;

    private final RestTemplate restTemplate;


    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.GOOGLE;
    }

    @Override
    public String requestAccessToken(OAuthLoginParams params) {



        String url = authUrl + "/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = params.makeBody();
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        body.add("code", ((GoogleLoginParams) params).getCode());

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        GoogleTokens response = restTemplate.postForObject(url, request, GoogleTokens.class);
        System.out.println("response.toString()");
        System.out.println(response.toString());

        System.out.println(response.getAccessToken());

        assert response != null;

        return response.getAccessToken();
    }

    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) {
        String url = authUrl + "/oauth2/v1/userinfo";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + accessToken);
        System.out.println("Authorization: " + "Bearer "+accessToken);
        HttpEntity request = new HttpEntity(httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                String.class
        );
        System.out.println("response.getBody() = " + response.getBody());
        System.out.println("===============================");

//        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        httpHeaders.set("access_token", accessToken);
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
//
//        OAuthInfoResponse oAuthInfoResponse = restTemplate.getForObject(url, GoogleInfoResponse.class);
//
//        System.out.println("=====================================");
//        System.out.println(oAuthInfoResponse.toString());

//        String url = authUrl + "/userinfo/v2/me";
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        httpHeaders.set("Authorization", "Bearer " + accessToken);
//
//        HttpEntity<?> request = new HttpEntity<>(httpHeaders);
//
//        ResponseEntity<GoogleInfoResponse> response = restTemplate.exchange(
//                url,
//                HttpMethod.GET,
//                request,
//                GoogleInfoResponse.class
//        );
        return null;

    }
}
