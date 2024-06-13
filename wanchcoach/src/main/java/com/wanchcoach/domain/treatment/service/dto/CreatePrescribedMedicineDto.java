package com.wanchcoach.domain.treatment.service.dto;

public record CreatePrescribedMedicineDto(Long drugId, Double quantity, Integer frequency, Integer day, String direction) {

    public static CreatePrescribedMedicineDto of(Long drugId, Double quantity, Integer frequency, Integer day, String direction) {
        return new CreatePrescribedMedicineDto(drugId, quantity, frequency, day, direction);
    }
}
