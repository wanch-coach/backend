package com.wanchcoach.domain.family.controller.request;

import java.time.LocalDate;

public record FamilyUpdateRequest(
        Long familyId,
        String name,
        LocalDate birthDate,
        String gender,
        String imageFileName
) {
}
