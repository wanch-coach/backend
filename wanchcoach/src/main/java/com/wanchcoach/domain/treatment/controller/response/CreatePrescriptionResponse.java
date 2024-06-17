package com.wanchcoach.domain.treatment.controller.response;

public record CreatePrescriptionResponse(Long prescriptionId) {
    public CreatePrescriptionResponse of(Long prescriptionId) {
        return new CreatePrescriptionResponse(prescriptionId);
    }
}