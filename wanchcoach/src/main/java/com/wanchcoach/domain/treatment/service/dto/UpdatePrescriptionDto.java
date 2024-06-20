package com.wanchcoach.domain.treatment.service.dto;

import com.wanchcoach.domain.medical.entity.Pharmacy;
import com.wanchcoach.domain.treatment.controller.dto.request.UpdatePrescriptionRequest;
import com.wanchcoach.domain.treatment.entity.Prescription;

import java.util.List;

public record UpdatePrescriptionDto(Long prescriptionId, Long familyId, Long pharmacyId, Boolean morning, Boolean noon, Boolean evening, Boolean beforeBed,
                                    List<UpdatePrescribedDrugDto> prescribedDrugs) {

    public static UpdatePrescriptionDto of(Long prescriptionId, UpdatePrescriptionRequest request) {
        if (request == null) return null;
        return new UpdatePrescriptionDto(
                prescriptionId,
                request.familyId(),
                request.pharmacyId(),
                request.morning(),
                request.noon(),
                request.evening(),
                request.beforeBed(),
                request.prescribedDrugs()
        );
    }

    public Prescription toEntity(Pharmacy pharmacy) {
        return Prescription.builder()
                .prescriptionId(prescriptionId())
                .pharmacy(pharmacy)
                .morning(morning())
                .noon(noon())
                .evening(evening())
                .beforeBed(beforeBed())
                .build();
    }

}
