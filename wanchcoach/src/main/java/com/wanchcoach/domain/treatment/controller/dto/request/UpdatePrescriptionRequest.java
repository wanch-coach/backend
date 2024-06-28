package com.wanchcoach.domain.treatment.controller.dto.request;

import com.wanchcoach.domain.treatment.service.dto.UpdatePrescribedDrugDto;

import java.util.List;

public record UpdatePrescriptionRequest(Long familyId, Long pharmacyId, Boolean morning, Boolean noon, Boolean evening, Boolean beforeBed,
                                        List<UpdatePrescribedDrugDto> prescribedDrugs) {

}
