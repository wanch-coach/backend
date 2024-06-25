package com.wanchcoach.domain.member.service.dto;

import com.wanchcoach.domain.member.controller.request.MemberUpdateInfoRequest;

import java.time.LocalDate;

public record MemberUpdateInfoDto(

        Long memberId,
        String name,
        String email,
        LocalDate birthDate,
        String gender,
        String phoneNumber
) {
    public static MemberUpdateInfoDto of(MemberUpdateInfoRequest memberUpdateInfoRequest, Long memberId) {
        return new MemberUpdateInfoDto(
                memberId,
                memberUpdateInfoRequest.getName(),
                memberUpdateInfoRequest.getEmail(),
                memberUpdateInfoRequest.getBirthDate(),
                memberUpdateInfoRequest.getGender(),
                memberUpdateInfoRequest.getPhoneNumber()
        );
    }
}
