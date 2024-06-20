package com.wanchcoach.domain.treatment.controller.dto.response;

public record CreateTreatmentResponse(Long treatmentId, Long prescriptionId) {

    public CreateTreatmentResponse of(Long treatmentId, Long prescriptionId) {
        return new CreateTreatmentResponse(treatmentId, prescriptionId);
    }
}
