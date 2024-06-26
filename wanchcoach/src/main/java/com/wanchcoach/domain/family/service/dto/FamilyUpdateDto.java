package com.wanchcoach.domain.family.service.dto;

import com.wanchcoach.domain.family.controller.request.FamilyUpdateRequest;

import java.time.LocalDate;

public record FamilyUpdateDto (
        Long familyId,
        String name,
        LocalDate birthDate,
        String gender,
        String imageFileName,
        String color
){
    public static FamilyUpdateDto of(FamilyUpdateRequest familyUpdateRequest){
        return new FamilyUpdateDto(
                familyUpdateRequest.getFamilyId(),
                familyUpdateRequest.getName(),
                familyUpdateRequest.getBirthDate(),
                familyUpdateRequest.getGender(),
                familyUpdateRequest.getImageFileName(),
                familyUpdateRequest.getColor()
        );
    }
}
