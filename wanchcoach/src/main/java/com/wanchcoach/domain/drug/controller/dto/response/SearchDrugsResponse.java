package com.wanchcoach.domain.drug.controller.dto.response;

public record SearchDrugsResponse(
        Long drugId,
        String itemName,
        String prductType,
        String drugImage,
        Long favorite
) {

}
