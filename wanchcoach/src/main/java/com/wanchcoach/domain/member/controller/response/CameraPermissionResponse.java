package com.wanchcoach.domain.member.controller.response;

import com.wanchcoach.domain.member.entity.Member;

public record CameraPermissionResponse(
        boolean cameraPermission
) {

    public static CameraPermissionResponse of(Member member) {
        return new CameraPermissionResponse(member.isCameraPermission());
    }
}
