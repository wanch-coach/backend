package com.wanchcoach.domain.medication.service.dto;

public record PrescriptionListDto(
        Long hospitalId,
        String hospitalName,
        String department,
        String start,
        String end,
        Long prescriptionId,
        boolean taking

) {

}
