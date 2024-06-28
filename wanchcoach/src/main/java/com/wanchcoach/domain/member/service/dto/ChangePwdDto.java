package com.wanchcoach.domain.member.service.dto;

import com.wanchcoach.domain.member.controller.request.ChangePwdRequest;

public record ChangePwdDto(
        Long MemberId,
        String pwd
) {
    public static ChangePwdDto of(Long memberId, ChangePwdRequest changePwdRequest) {
        return new ChangePwdDto(memberId, changePwdRequest.pwd());
    }
}
