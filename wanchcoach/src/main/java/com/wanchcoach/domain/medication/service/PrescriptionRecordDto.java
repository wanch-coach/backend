package com.wanchcoach.domain.medication.service;

public record PrescriptionRecordDto(
        Long hospitalId,
        String hospitalName,
        String department,
        String start,
        String end,
        Long prescriptionId
) {
}
