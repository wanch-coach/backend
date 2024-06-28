package com.wanchcoach.domain.member.service.dto;

import com.wanchcoach.domain.member.controller.request.FindMemberLoginIdRequest;

import java.time.LocalDate;

public record FindMemberLoginIdDto(
        String name,
        String phoneNumber,
        LocalDate birthDate
) {

    public static FindMemberLoginIdDto of(FindMemberLoginIdRequest findMemberLoginIdRequest) {
        return new FindMemberLoginIdDto(
                findMemberLoginIdRequest.name(),
                findMemberLoginIdRequest.phoneNumber(),
                findMemberLoginIdRequest.birthDate()
        );
    }
}
