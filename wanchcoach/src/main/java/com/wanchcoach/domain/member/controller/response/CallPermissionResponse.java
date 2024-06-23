package com.wanchcoach.domain.member.controller.response;

import com.wanchcoach.domain.member.entity.Member;

public record CallPermissionResponse(
        boolean callPermission
) {
    public static CallPermissionResponse of(Member member) {
        return new CallPermissionResponse(member.isCallPermission());
    }
}
