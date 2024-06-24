package com.wanchcoach.domain.member.controller.response;

public record FindIdResponse(
        String LoginId
) {
    public static FindIdResponse of(String loginId) {
        return new FindIdResponse(loginId);
    }
}
