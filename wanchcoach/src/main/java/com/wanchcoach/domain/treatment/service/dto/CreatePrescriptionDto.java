package com.wanchcoach.domain.treatment.service.dto;

import java.util.List;

public record CreatePrescriptionDto(Long familyId, Long pharmacyId, Boolean morning, Boolean noon, Boolean evening, Boolean beforeBed,
                                    List<CreatePrescribedMedicineDto> prescribedMedicines) {

    public static CreatePrescriptionDto of(Long familyId, Long pharmacyId, Boolean morning, Boolean noon, Boolean evening, Boolean beforeBed,
                                           List<CreatePrescribedMedicineDto> prescribedMedicines) {
        return new CreatePrescriptionDto(familyId, pharmacyId, morning, noon, evening, beforeBed, prescribedMedicines);
    }
}
