package com.wanchcoach.domain.medication.service.dto;

import com.wanchcoach.domain.medication.controller.request.TakingMedicineRequest;

import java.time.LocalDate;

public record TakingMedicineDto(
        Long prescriptionId,
        Long familyId,
        int time,
        LocalDate takenDate
) {
    public static TakingMedicineDto of(Long prescriptionId, TakingMedicineRequest request){

        if(request.time().equals("morning")){
            return new TakingMedicineDto(prescriptionId, request.familyId(), 0, request.takenDate());
        }else if(request.time().equals("noon")){
            return new TakingMedicineDto(prescriptionId, request.familyId(), 1, request.takenDate());
        }else if(request.time().equals("evening")){
            return new TakingMedicineDto(prescriptionId, request.familyId(), 2, request.takenDate());
        }else{
            return new TakingMedicineDto(prescriptionId, request.familyId(), 3, request.takenDate());
        }
    }
}
