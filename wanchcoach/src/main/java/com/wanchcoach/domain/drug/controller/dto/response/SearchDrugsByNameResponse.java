package com.wanchcoach.domain.drug.controller.dto.response;

import com.wanchcoach.domain.drug.service.dto.SearchDrugsSimpleDto;

public record SearchDrugsByNameResponse(
        Long drugId,
        String itemName
) {
    public static SearchDrugsByNameResponse of(SearchDrugsSimpleDto searchDrugsSimpleDto){
        return new SearchDrugsByNameResponse(searchDrugsSimpleDto.getDrugId(), searchDrugsSimpleDto.getItemName());
    }
}
