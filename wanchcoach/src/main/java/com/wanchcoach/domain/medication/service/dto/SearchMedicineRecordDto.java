package com.wanchcoach.domain.medication.service.dto;

import java.time.LocalDateTime;

public record SearchMedicineRecordDto(
        LocalDateTime takenTime;
) {
}
