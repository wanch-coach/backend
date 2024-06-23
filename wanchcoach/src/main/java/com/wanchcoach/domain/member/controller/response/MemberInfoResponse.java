package com.wanchcoach.domain.member.controller.response;

import com.wanchcoach.domain.member.entity.Member;

import java.time.LocalDate;

public record MemberInfoResponse(
        Long memberId,
        String name,
        String email,
        LocalDate birthDate,
        String gender,
        String phoneNumber
) {
    public static MemberInfoResponse of(Member member) {
        return new MemberInfoResponse(
                member.getMemberId(),
                member.getName(),
                member.getEmail(),
                member.getBirthDate(),
                member.getGender(),
                member.getPhoneNumber()
        );
    }
}
