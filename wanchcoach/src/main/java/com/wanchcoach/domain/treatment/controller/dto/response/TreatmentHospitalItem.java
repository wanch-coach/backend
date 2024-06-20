package com.wanchcoach.domain.treatment.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TreatmentHospitalItem {
    Long hospitalId;
    String hospitalName;
    List<TreatmentItem> treatmentItems;
}
