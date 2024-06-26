package com.wanchcoach.domain.medication.service.dto;

import java.time.LocalDateTime;

public record DailyPrescriptionDto(
        int remains,
        String hospitalName,
        String department,
        Long prescriptionId,
        boolean morning,
        boolean noon,
        boolean evening,
        boolean beforeBed

        ) {
}
