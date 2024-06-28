package com.wanchcoach.domain.auth.infoResponse;


import com.wanchcoach.domain.auth.application.OAuthProvider;

import java.time.LocalDate;

public interface OAuthInfoResponse {

    default String getEmail() {
        return null;
    }

    default String getName() {
        return null;
    }

    default String getGender() {
        return null;
    }

    default LocalDate getBirthday() {
        return null;
    }

    default String getProfile_Image() {
        return null;
    }

    OAuthProvider getOAuthProvider();

    default String getMobile() {
        return null;
    }
}
