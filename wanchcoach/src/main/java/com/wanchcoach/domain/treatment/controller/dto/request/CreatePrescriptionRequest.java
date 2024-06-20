package com.wanchcoach.domain.treatment.controller.dto.request;

import com.wanchcoach.domain.treatment.service.dto.CreatePrescribedDrugDto;

import java.util.List;

public record CreatePrescriptionRequest (Long familyId, Long pharmacyId, Boolean morning, Boolean noon, Boolean evening, Boolean beforeBed,
                                         List<CreatePrescribedDrugDto> prescribedDrugs) {

}
