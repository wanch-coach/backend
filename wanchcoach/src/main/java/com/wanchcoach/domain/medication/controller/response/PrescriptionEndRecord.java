package com.wanchcoach.domain.medication.controller.response;

import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugsResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PrescriptionEndRecord(
        Long hospitalId,
        String hospitalName,
        String department,
        LocalDateTime start,
        LocalDate end,
        Long prescriptionId,
        List<SearchDrugsResponse> drugs
) {
}
