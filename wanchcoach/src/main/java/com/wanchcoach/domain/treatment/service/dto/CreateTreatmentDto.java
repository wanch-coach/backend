package com.wanchcoach.domain.treatment.service.dto;

import com.wanchcoach.domain.treatment.entity.Treatment;

public record CreateTreatmentDto(Long familyId, Long hospitalId, Long prescriptionId, String department,
                                 String date, Boolean taken, Boolean alarm, String symptom) {

    public static CreateTreatmentDto of(Long familyId, Long hospitalId, Long prescriptionId, String department,
                                        String date, Boolean taken, Boolean alarm, String symptom) {
        return new CreateTreatmentDto(familyId, hospitalId, prescriptionId, department, date, taken, alarm, symptom);
    }
}
