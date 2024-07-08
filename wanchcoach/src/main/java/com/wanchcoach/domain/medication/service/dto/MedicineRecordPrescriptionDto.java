package com.wanchcoach.domain.medication.service.dto;

import java.time.LocalDate;

public record MedicineRecordPrescriptionDto(
    Long hospitalId,
    String hospitalName,
    String department,
    Long prescriptionId,
    LocalDate takenDay,
    int time
) {
}
