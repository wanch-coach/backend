package com.wanchcoach.domain.treatment.service.dto;

public record DeleteTreatmentDto(Long treatmentId) {
    public static DeleteTreatmentDto of(Long treatmentId) {
        return new DeleteTreatmentDto(treatmentId);
    }
}
