package com.wanchcoach.domain.treatment.service.dto;

import java.util.List;

public record CreatePrescriptionDto(Long pharmacyId, Boolean morning, Boolean noon, Boolean evening, Boolean beforeBed,
                                    List<CreatePrescribedMedicineDto> prescribedMedicines) {

    public static CreatePrescriptionDto of(Long pharmacyId, Boolean morning, Boolean noon, Boolean evening, Boolean beforeBed,
                                           List<CreatePrescribedMedicineDto> prescribedMedicines) {
        return new CreatePrescriptionDto(pharmacyId, morning, noon, evening, beforeBed, prescribedMedicines);
    }
}
