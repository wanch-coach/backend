package com.wanchcoach.domain.medication.service.dto;

import com.wanchcoach.domain.medication.controller.request.GetPillsRequest;

import java.time.LocalDate;

public record GetPillsDto(
        Long familyId,
        LocalDate startDate,
        LocalDate endDate
) {

    public static GetPillsDto of(Long familyId, GetPillsRequest request){
        return new GetPillsDto(familyId, request.startDate(), request.endDate());
    }
}
