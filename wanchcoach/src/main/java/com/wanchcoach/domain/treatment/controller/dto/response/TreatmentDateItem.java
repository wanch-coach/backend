package com.wanchcoach.domain.treatment.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class TreatmentDateItem {
    private LocalDate date;
    private List<TreatmentItem> treatmentItems;
}
