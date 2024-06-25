package com.wanchcoach.domain.medication.controller.request;

public record TakingMedicineRequest (
        Long familyId,
        String time
){
}
