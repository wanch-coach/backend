package com.wanchcoach.domain.member.service.dto;

import com.wanchcoach.domain.member.controller.request.MemberLoginRequest;

public record MemberLoginDto (
        String loginId,
        String pwd){

    public static MemberLoginDto of(MemberLoginRequest memberLoginRequest) {
        return new MemberLoginDto(memberLoginRequest.id(), memberLoginRequest.pwd());
    }
}
