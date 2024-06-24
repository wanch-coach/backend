package com.wanchcoach.domain.medication.controller.response;

import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugsResponse;

import java.util.List;

public record PrescriptionEndRecord(
        Long hospitalId,
        String hospitalName,
        String department,
        String start,
        String end,
        List<SearchDrugsResponse> drugs
) {
}
