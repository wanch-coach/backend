package com.wanchcoach.domain.member.controller.request;

import com.wanchcoach.domain.auth.application.OAuthProvider;
import com.wanchcoach.domain.member.entity.Member;

import java.time.LocalDate;

/**
 * TO DO
 * 회원가입 시 요구하지 않는 값 제거 필요
 */
public record MemberSignupRequest(
        String loginId,
        String pwd,
        String name,
        String email,
        LocalDate birthDate,
        String gender,
        String phoneNumber
//        boolean active,
//        boolean loginType,
//        String refreshToken,
//        boolean locationPermission,
//        boolean callPermission,
//        boolean cameraPermission,
//        OAuthProvider oAuthProvider
) {

//    public static MemberSignupRequest of(String loginId,
//                                         String encryptedPwd,
//                                         String name,
//                                         String email,
//                                         LocalDate birthDate,
//                                         String gender,
//                                         String phoneNumber,
//                                         boolean active,
//                                         boolean loginType,
//                                         String refreshToken,
//                                         boolean locationPermission,
//                                         boolean callPermission,
//                                         boolean cameraPermission) {
//        return new MemberSignupRequest(loginId,
//                encryptedPwd,
//                name,
//                email,
//                birthDate,
//                gender,
//                phoneNumber,
//                active,
//                loginType,
//                refreshToken,
//                locationPermission,
//                callPermission,
//                cameraPermission);
//    }

//    // from 메서드 구현
//    public static MemberSignupRequest from(Member member) {
//        return new MemberSignupRequest(member.getLoginId(),
//                member.getEncryptedPwd(),
//                member.getName(),
//                member.getEmail(),
//                member.getBirthDate(),
//                member.getGender(),
//                member.getPhoneNumber(),
//                member.isActive(),
//                member.isLoginType(),
//                member.getRefreshToken(),
//                member.isLocationPermission(),
//                member.isCallPermission(),
//                member.isCameraPermission());
//    }

    // toEntity 메서드 구현
//    public Member toEntity() {
//        return Member.builder()
//                .loginId(loginId)
//                .encryptedPwd(encryptedPwd)
//                .name(name)
//                .email(email)
//                .birthDate(birthDate)
//                .gender(gender)
//                .phoneNumber(phoneNumber)
//                .active(active)
//                .loginType(loginType)
//                .refreshToken(refreshToken)
//                .locationPermission(locationPermission)
//                .callPermission(callPermission)
//                .cameraPermission(cameraPermission)
//                .build();
//    }
}
