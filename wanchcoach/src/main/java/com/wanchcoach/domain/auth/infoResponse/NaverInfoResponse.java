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
public class NaverInfoResponse implements OAuthInfoResponse {

    @JsonProperty("response")
    private Response response;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Response {
        private String email;
        private String name;
    }

    @Override
    public String getEmail() {
        return response.email;
    }

    @Override
    public String getName() {
        return response.name;
    }

    @Override
    public String getGender() {
        return "null";
    }


    @Override
    public LocalDate getBirthday() {
        LocalDate localDate = LocalDate.of(2020, 1, 1);
        return localDate;
    }

    @Override
    public String getProfile_Image() {
        return "null";
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.NAVER;
    }
}
