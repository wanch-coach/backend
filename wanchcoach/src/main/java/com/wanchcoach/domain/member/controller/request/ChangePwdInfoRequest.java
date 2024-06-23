package com.wanchcoach.domain.member.controller.request;

import java.time.LocalDate;

public record ChangePwdInfoRequest(
        String name,
        String loginId,
        String phoneNumber,
        LocalDate birthDate
) {
}
