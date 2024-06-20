package com.wanchcoach.domain.treatment.service.dto;

import com.wanchcoach.domain.family.entity.Family;
import com.wanchcoach.domain.medical.entity.Hospital;
import com.wanchcoach.domain.treatment.controller.dto.request.UpdateTreatmentRequest;
import com.wanchcoach.domain.treatment.entity.Treatment;

import java.time.LocalDateTime;

public record UpdateTreatmentDto(Long treatmentId, Long familyId, Long hospitalId, String department,
                                 String date, Boolean taken, Boolean alarm, String symptom, UpdatePrescriptionDto prescription) {

    public static UpdateTreatmentDto of(Long treatmentId, UpdateTreatmentRequest request) {
        return new UpdateTreatmentDto(
                treatmentId,
                request.familyId(),
                request.hospitalId(),
                request.department(),
                request.date(),
                request.taken(),
                request.alarm(),
                request.symptom(),
                UpdatePrescriptionDto.of(1L, request.prescription())
        );
    }

    public Treatment toEntity(Family family, Hospital hospital) {
        return Treatment.builder()
                .treatmentId(treatmentId)
                .family(family)
                .hospital(hospital)
                .prescription(null)
                .department(department())
                .date(LocalDateTime.parse(date()))
                .taken(taken())
                .alarm(alarm())
                .symptom(symptom())
            .build();
    }
}
