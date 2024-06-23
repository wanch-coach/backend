package com.wanchcoach.domain.member.controller.response;

import com.wanchcoach.domain.member.entity.Member;

public record LocationPermissionResponse(
        boolean locationPermission
) {
    public static LocationPermissionResponse of(Member member) {
        return new LocationPermissionResponse(member.isLocationPermission());
    }
}
