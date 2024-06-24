package com.wanchcoach.domain.medication.controller.response;

import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugsResponse;

import java.util.List;

public record PrescriptionTakingRecord(
        Long hospitalId,
        String hospitalName,
        String department,
        String start,
        Long prescriptionId,
        List<SearchDrugsResponse> drugs
) {
}
