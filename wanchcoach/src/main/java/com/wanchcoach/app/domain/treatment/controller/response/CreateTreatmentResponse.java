package com.wanchcoach.app.domain.treatment.controller.response;

public record CreateTreatmentResponse(Long treatmentId) {

    public CreatePrescriptionResponse of(Long treatmentId) {
        return new CreatePrescriptionResponse(treatmentId);
    }
}
