package com.wanchcoach.domain.member.controller.response;

import com.wanchcoach.domain.member.entity.Member;

public record AlarmPermissionResponse(
        boolean alarmPermission
) {
    public static AlarmPermissionResponse of(Member member) {
        return new AlarmPermissionResponse(member.isAlarmPermission());
    }
}
