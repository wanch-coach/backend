package com.wanchcoach.domain.drug.controller.dto.response;

public record SearchFavoritesResponse(
        Long favoriteId,
        Long drugId,
        String itemName,
        String spdtyPblc,
        String drugImage
){
}
