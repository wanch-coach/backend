package com.wanchcoach.domain.treatment.service.dto;

import com.wanchcoach.domain.treatment.entity.PrescribedDrug;

public record PrescribedDrugDetailDto(Long drugId, String name, Double quantity, Integer frequency, Integer day, String direction) {

    public static PrescribedDrugDetailDto of(PrescribedDrug prescribedDrug) {
        return new PrescribedDrugDetailDto(
                prescribedDrug.getDrug().getDrugId(),
                prescribedDrug.getDrug().getItemName(),
                prescribedDrug.getQuantity(),
                prescribedDrug.getFrequency(),
                prescribedDrug.getDay(),
                prescribedDrug.getDirection()
        );
    }
}
