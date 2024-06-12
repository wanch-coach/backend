package com.wanchcoach.domain.treatment.controller.request;

import com.wanchcoach.domain.treatment.service.dto.CreatePrescribedMedicineDto;

import java.util.List;

public record CreatePrescriptionRequest (Long pharmacyId, Boolean morning, Boolean noon, Boolean evening, Boolean beforeBed,
                                         List<CreatePrescribedMedicineDto> prescribedMedicines) {

}
