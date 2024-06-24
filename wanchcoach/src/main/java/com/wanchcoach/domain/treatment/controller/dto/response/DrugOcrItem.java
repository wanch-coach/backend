package com.wanchcoach.domain.treatment.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DrugOcrItem {

    private String iCode;
    private String itemName;
    private Double quantity;
    private Integer frequency;
    private Integer day;
    private String direction;
}
