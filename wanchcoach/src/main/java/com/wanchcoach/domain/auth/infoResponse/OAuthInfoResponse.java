package com.wanchcoach.domain.auth.infoResponse;


import com.wanchcoach.domain.auth.application.OAuthProvider;

import java.time.LocalDate;

public interface OAuthInfoResponse {

    String getEmail();
    String getName();
    String getGender();
    LocalDate getBirthday();
    String getProfile_Image();
    OAuthProvider getOAuthProvider();

}
