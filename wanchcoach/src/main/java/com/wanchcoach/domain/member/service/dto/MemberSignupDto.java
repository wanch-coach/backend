package com.wanchcoach.domain.member.service.dto;

import com.wanchcoach.domain.member.controller.request.MemberSignupRequest;
import com.wanchcoach.domain.member.entity.Member;

import java.time.LocalDate;

public record MemberSignupDto(String loginId,
                              String pwd,
                              String name,
                              String email,
                              LocalDate birthDate,
                              String gender,
                              String phoneNumber,
                              boolean active,
                              boolean loginType,
                              String refreshToken,
                              boolean locationPermission,
                              boolean callPermission,
                              boolean cameraPermission) {

    public static MemberSignupDto of(MemberSignupRequest memberSignupRequest) {
        return new MemberSignupDto(memberSignupRequest.loginId(),
                memberSignupRequest.pwd(),
                memberSignupRequest.name(),
                memberSignupRequest.email(),
                memberSignupRequest.birthDate(),
                memberSignupRequest.gender(),
                memberSignupRequest.phoneNumber(),
                true,
                false,
                "",
                false,
                false,
                false);
    }

    public Member toEntity(String encryptPwd) {
        return Member.builder()
                .loginId(loginId)
                .encryptedPwd(encryptPwd)
                .name(name)
                .email(email)
                .birthDate(birthDate)
                .gender(gender)
                .phoneNumber(phoneNumber)
                .active(active)
                .loginType(loginType)
                .refreshToken(refreshToken)
                .locationPermission(locationPermission)
                .callPermission(callPermission)
                .cameraPermission(cameraPermission)
                .build();
    }
}
