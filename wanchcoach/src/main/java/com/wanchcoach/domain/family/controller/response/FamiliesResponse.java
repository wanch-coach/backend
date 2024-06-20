package com.wanchcoach.domain.family.controller.response;

import com.wanchcoach.domain.family.entity.Family;

public record FamiliesResponse(
        Long familyId,
        String name
) {
    public static FamiliesResponse from(Family family) {
        return new FamiliesResponse(family.getFamilyId(), family.getName());
    }
}