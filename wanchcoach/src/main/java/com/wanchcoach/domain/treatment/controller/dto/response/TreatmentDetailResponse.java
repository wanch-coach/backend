package com.wanchcoach.domain.treatment.controller.dto.response;

import com.wanchcoach.domain.treatment.entity.PrescribedDrug;
import com.wanchcoach.domain.treatment.entity.Prescription;
import com.wanchcoach.domain.treatment.entity.Treatment;

import java.util.List;

public record TreatmentDetailResponse(Long hospitalId, String hospitalName, Long familyId, String familyName, String department,
                                      String date, Boolean taken, Boolean alarm, String symptom, PrescriptionDetailResponse prescription) {

    public static TreatmentDetailResponse of(Treatment treatment) {
        return new TreatmentDetailResponse(
                treatment.getHospital().getHospitalId(),
                treatment.getHospital().getName(),
                treatment.getFamily().getFamilyId(),
                treatment.getFamily().getName(),
                treatment.getDepartment(),
                treatment.getDate().toString(),
                treatment.getTaken(),
                treatment.getAlarm(),
                treatment.getSymptom(),
                null
        );
    }
    public static TreatmentDetailResponse of(Treatment treatment, Prescription prescription, List<PrescribedDrug> prescribedDrugs) {
        return new TreatmentDetailResponse(
                treatment.getHospital().getHospitalId(),
                treatment.getHospital().getName(),
                treatment.getFamily().getFamilyId(),
                treatment.getFamily().getName(),
                treatment.getDepartment(),
                treatment.getDate().toString(),
                treatment.getTaken(),
                treatment.getAlarm(),
                treatment.getSymptom(),
                PrescriptionDetailResponse.of(prescription, prescribedDrugs)
        );
    }
}
