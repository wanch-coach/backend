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
        private String gender;
        private String birthday;
        private String birthyear;
        private String mobile;
        private String profile_image;
    }


    @Override
    public String getMobile(){
        return response.mobile;
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
        return response.gender;
    }


    @Override
    public LocalDate getBirthday() {
        String[] parts = response.birthday.split("-");
        int dayOfMonth = Integer.parseInt(parts[1]);
        int monthValue = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(response.birthyear);
        return LocalDate.of(year, monthValue, dayOfMonth);
    }

    @Override
    public String getProfile_Image() {
        return response.getProfile_image();
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.NAVER;
    }
}
