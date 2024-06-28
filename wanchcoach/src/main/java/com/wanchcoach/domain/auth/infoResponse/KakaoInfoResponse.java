package com.wanchcoach.domain.auth.infoResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanchcoach.domain.auth.application.OAuthProvider;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class KakaoInfoResponse implements OAuthInfoResponse{


    @JsonProperty("kakao_account")
    private KakaoInfoResponse.Response response;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Response{
        private String email;
    }

    @Override
    public String getEmail() {
        return response.getEmail();
    }


    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }
}
