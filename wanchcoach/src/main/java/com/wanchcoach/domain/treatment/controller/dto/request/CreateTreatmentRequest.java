package com.wanchcoach.domain.treatment.controller.dto.request;

public record CreateTreatmentRequest(Long hospitalId, Long familyId, String department,
                                     String date, Boolean taken, Boolean alarm, String symptom, CreatePrescriptionRequest prescription) {

}
