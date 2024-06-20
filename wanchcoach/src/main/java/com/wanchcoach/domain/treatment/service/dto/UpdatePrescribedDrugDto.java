package com.wanchcoach.domain.treatment.service.dto;

import com.wanchcoach.domain.drug.entity.Drug;
import com.wanchcoach.domain.treatment.entity.PrescribedDrug;
import com.wanchcoach.domain.treatment.entity.Prescription;

public record UpdatePrescribedDrugDto(Long drugId, Double quantity, Integer frequency, Integer day, String direction) {

    public static UpdatePrescribedDrugDto of(Long drugId, Double quantity, Integer frequency, Integer day, String direction) {
        return new UpdatePrescribedDrugDto(drugId, quantity, frequency, day, direction);
    }

    public PrescribedDrug toEntity(Prescription prescription, Drug drug) {
        return PrescribedDrug.builder()
                .prescription(prescription)
                .drug(drug)
                .quantity(quantity())
                .frequency(frequency())
                .day(day())
                .direction(direction())
                .build();
    }
}
