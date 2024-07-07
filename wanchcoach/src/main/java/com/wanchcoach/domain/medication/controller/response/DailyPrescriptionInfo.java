package com.wanchcoach.domain.medication.controller.response;

import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugsResponse;

import java.util.List;

public record DailyPrescriptionInfo(
        boolean alarm,
        Long prescriptionId,
        String hospitalName,
        String department,
        int remains,
        List<SearchDrugsResponse> drugs
) {

}
