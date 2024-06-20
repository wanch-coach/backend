package com.wanchcoach.domain.treatment.controller.dto.response;

public record CreatePrescriptionResponse(Long prescriptionId) {
    public CreatePrescriptionResponse of(Long prescriptionId) {
        return new CreatePrescriptionResponse(prescriptionId);
    }
}
