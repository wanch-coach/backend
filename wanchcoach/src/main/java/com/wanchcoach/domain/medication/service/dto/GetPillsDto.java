package com.wanchcoach.domain.medication.service.dto;

import java.time.LocalDate;

public record GetPillsDto(
        Long familyId,
        LocalDate startDate,
        LocalDate endDate
) {

    public static GetPillsDto of(Long familyId, LocalDate startDate, LocalDate endDate){
        return new GetPillsDto(familyId, startDate, endDate);
    }
}
