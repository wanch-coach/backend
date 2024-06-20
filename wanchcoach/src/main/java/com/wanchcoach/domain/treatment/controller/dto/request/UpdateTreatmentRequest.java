package com.wanchcoach.domain.treatment.controller.dto.request;

public record UpdateTreatmentRequest(Long hospitalId, Long familyId, String department,
                                     String date, Boolean taken, Boolean alarm, String symptom, UpdatePrescriptionRequest prescription) {

}
