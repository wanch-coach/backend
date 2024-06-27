package com.wanchcoach.domain.medication.controller.response;

import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugsResponse;

import java.util.List;

public record RecordCalendarDayInfo(
        String hospitalName,
        String department,
        Long prescriptionId,
        List<SearchDrugsResponse> drugs
) {
}
