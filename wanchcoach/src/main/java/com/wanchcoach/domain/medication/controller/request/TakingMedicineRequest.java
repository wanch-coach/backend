package com.wanchcoach.domain.medication.controller.request;

import java.time.LocalDate;

public record TakingMedicineRequest (
        Long familyId,

        LocalDate takenDate,
        String time
){
}
