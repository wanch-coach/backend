package com.wanchcoach.domain.auth.controller.response;

import com.wanchcoach.domain.auth.infoResponse.OAuthInfoResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
@ToString
@Getter
@Setter
public class AuthSignupResponse extends SocialResponse{

    boolean isSignup;
    String loginId;
    String name;
    String email;
    LocalDate birthDate;
    String gender;
    String phoneNumber;


    public AuthSignupResponse(boolean isSignup,String loginId, String name, String email, LocalDate birthDate, String gender, String phoneNumber) {
        super("signupInfo");
        this.isSignup = isSignup;
        this.loginId = loginId;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    public static AuthSignupResponse of(OAuthInfoResponse oAuthInfoResponse, String loginId) {
        return new AuthSignupResponse(
                false,
                loginId,
                oAuthInfoResponse.getName(),
                oAuthInfoResponse.getEmail(),
                oAuthInfoResponse.getBirthday(),
                oAuthInfoResponse.getGender(),
                oAuthInfoResponse.getMobile()
        );
    }
}
