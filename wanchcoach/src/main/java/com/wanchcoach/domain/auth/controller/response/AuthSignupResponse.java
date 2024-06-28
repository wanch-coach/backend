package com.wanchcoach.domain.auth.controller.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanchcoach.domain.auth.infoResponse.OAuthInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
@ToString
@Getter
@Setter
public class AuthSignupResponse extends SocialResponse{


    String name;
    String email;
    LocalDate birthDate;
    String gender;
    String phoneNumber;


    public AuthSignupResponse(String name, String email, LocalDate birthDate, String gender, String phoneNumber) {
        super("signupInfo");
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    public static AuthSignupResponse of(OAuthInfoResponse oAuthInfoResponse) {
        return new AuthSignupResponse(
                oAuthInfoResponse.getName(),
                oAuthInfoResponse.getEmail(),
                oAuthInfoResponse.getBirthday(),
                oAuthInfoResponse.getGender(),
                oAuthInfoResponse.getMobile()
        );
    }
}
