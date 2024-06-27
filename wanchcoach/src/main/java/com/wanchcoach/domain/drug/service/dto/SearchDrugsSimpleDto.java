package com.wanchcoach.domain.drug.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchDrugsSimpleDto {
    private Long drugId;
    private String itemName;
}
