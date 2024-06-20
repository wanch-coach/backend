package com.wanchcoach.domain.treatment.service.dto;

import com.wanchcoach.domain.medical.entity.Pharmacy;
import com.wanchcoach.domain.treatment.controller.dto.request.CreatePrescriptionRequest;
import com.wanchcoach.domain.treatment.entity.Prescription;

import java.util.List;

public record CreatePrescriptionDto(Long familyId, Long pharmacyId, Boolean morning, Boolean noon, Boolean evening, Boolean beforeBed,
                                    Boolean active, List<CreatePrescribedDrugDto> prescribedDrugs) {

    public static CreatePrescriptionDto of(CreatePrescriptionRequest request) {
        return new CreatePrescriptionDto(
                request.familyId(),
                request.pharmacyId(),
                request.morning(),
                request.noon(),
                request.evening(),
                request.beforeBed(),
                true,
                request.prescribedDrugs()
        );
    }

    public Prescription toEntity(Pharmacy pharmacy, int maxRemains) {
        return Prescription.builder()
                .pharmacy(pharmacy)
                .remains(maxRemains)
                .taking(true)
                .endDate(null)
                .morning(morning())
                .noon(noon())
                .evening(evening())
                .beforeBed(beforeBed())
                .active(true)
                .build();
    }
}
