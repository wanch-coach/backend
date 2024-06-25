package com.wanchcoach.domain.member.controller.request;

import java.time.LocalDate;

public record FindMemberLoginIdRequest(
        String name,
        String phoneNumber,
        LocalDate birthDate
) {
}
