package com.wanchcoach.domain.treatment.service.dto;

public record CreateTreatmentDto(Long familyId, Long hospitalId, String department,
                                 String date, Boolean taken, Boolean alarm, String symptom) {

    public static CreateTreatmentDto of(Long familyId, Long hospitalId, String department,
                                        String date, Boolean taken, Boolean alarm, String symptom) {
        return new CreateTreatmentDto(familyId, hospitalId, department, date, taken, alarm, symptom);
    }
}
