package com.wanchcoach.domain.treatment.controller.dto.response;

public record UpdateTreatmentResponse(Long treatmentId, Long prescriptionId) {

    public UpdateTreatmentResponse of(Long treatmentId, Long prescriptionId) {
        return new UpdateTreatmentResponse(treatmentId, prescriptionId);
    }
}
