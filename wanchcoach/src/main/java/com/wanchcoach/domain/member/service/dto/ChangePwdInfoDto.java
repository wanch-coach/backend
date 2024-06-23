package com.wanchcoach.domain.member.service.dto;

import com.wanchcoach.domain.member.controller.request.ChangePwdInfoRequest;

import java.time.LocalDate;

public record ChangePwdInfoDto(

        String name,
        String loginId,
        String phoneNumber,
        LocalDate birthDate
) {
    public static ChangePwdInfoDto of(ChangePwdInfoRequest changePwdInfoRequest) {
        return new ChangePwdInfoDto(
                changePwdInfoRequest.name(),
                changePwdInfoRequest.loginId(),
                changePwdInfoRequest.phoneNumber(),
                changePwdInfoRequest.birthDate()
        );
    }
}
