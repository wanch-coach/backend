package com.wanchcoach.domain.family.controller.response;

import com.wanchcoach.domain.family.entity.Family;

import java.time.LocalDate;

public record FamilyInfoResponse (
        Long familyId,
        String name,
        LocalDate birthDate,
        String gender,
        String imageFileName,
        String color
){
    public static FamilyInfoResponse from(Family family) {
        return new FamilyInfoResponse(
                family.getFamilyId(),
                family.getName(),
                family.getBirthDate(),
                family.getGender(),
                family.getImageFileName(),
                family.getColor());
    }
}
