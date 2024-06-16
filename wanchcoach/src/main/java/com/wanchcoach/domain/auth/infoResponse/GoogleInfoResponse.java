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
public class GoogleInfoResponse implements OAuthInfoResponse{

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
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getGender() {
        return null;
    }

    @Override
    public LocalDate getBirthday() {
        return null;
    }

    @Override
    public String getProfile_Image() {
        return null;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return null;
    }
}
