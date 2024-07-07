package com.wanchcoach.domain.auth.controller.response;

public record TokenResponse (String token) {

    public static TokenResponse of(String accessToken) {
        return new TokenResponse(accessToken);
    }
}
