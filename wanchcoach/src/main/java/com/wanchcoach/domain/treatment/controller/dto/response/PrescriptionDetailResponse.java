package com.wanchcoach.domain.treatment.controller.dto.response;

import com.wanchcoach.domain.treatment.entity.PrescribedDrug;
import com.wanchcoach.domain.treatment.entity.Prescription;
import com.wanchcoach.domain.treatment.service.dto.PrescribedDrugDetailDto;

import java.util.List;

public record PrescriptionDetailResponse(Long pharmacyId, String pharmacyName, Boolean morning, Boolean noon, Boolean evening, Boolean beforeBed,
                                         List<PrescribedDrugDetailDto> prescribedDrugs) {

    public static PrescriptionDetailResponse of(Prescription prescription, List<PrescribedDrug> prescribedDrugs) {
        return new PrescriptionDetailResponse(
                prescription.getPharmacy().getPharmacyId(),
                prescription.getPharmacy().getName(),
                prescription.getMorning(),
                prescription.getNoon(),
                prescription.getEvening(),
                prescription.getBeforeBed(),
                prescribedDrugs.stream().map(PrescribedDrugDetailDto::of).toList()
        );
    }
}
