package com.wanchcoach.domain.medication.service.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PrescriptionListDto(
        Long hospitalId,
        String hospitalName,
        String department,
        LocalDateTime start,
        LocalDate end,
        Long prescriptionId,
        boolean taking

) {

}
