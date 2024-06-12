package com.wanchcoach.app.domain.treatment.service.dto;

public record CreatePrescribedMedicineDto(Long medicineId, Double Quantity, Integer frequency, Integer day, String direction) {

    public static CreatePrescribedMedicineDto of(Long medicineId, Double Quantity, Integer frequency, Integer day, String direction) {
        return new CreatePrescribedMedicineDto(medicineId, Quantity, frequency, day, direction);
    }
}
