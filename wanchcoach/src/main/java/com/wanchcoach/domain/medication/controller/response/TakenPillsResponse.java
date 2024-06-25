package com.wanchcoach.domain.medication.controller.response;

import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugsResponse;
import com.wanchcoach.domain.medication.service.dto.SearchMedicineRecordDto;

import java.util.List;

public record TakenPillsResponse(
        SearchDrugsResponse drugInfo,
        List<SearchMedicineRecordDto> records
) {
}
