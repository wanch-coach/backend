package com.wanchcoach.domain.medication.service.dto;

import java.time.LocalDateTime;

public record MedicineRecordPrescriptionDto(
    Long hospitalId,
    String hospitalName,
    String department,
    Long prescriptionId,
    LocalDateTime takenDay,
    String time
) {
}
