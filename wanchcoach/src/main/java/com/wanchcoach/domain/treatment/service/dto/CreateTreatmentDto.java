package com.wanchcoach.domain.treatment.service.dto;

import com.wanchcoach.domain.family.entity.Family;
import com.wanchcoach.domain.medical.entity.Hospital;
import com.wanchcoach.domain.treatment.controller.dto.request.CreateTreatmentRequest;
import com.wanchcoach.domain.treatment.entity.Treatment;

import java.time.LocalDateTime;

public record CreateTreatmentDto(Long familyId, Long hospitalId, String department,
                                 String date, Boolean taken, Boolean alarm, String symptom, Boolean active) {


    public static CreateTreatmentDto of(CreateTreatmentRequest request) {
        return new CreateTreatmentDto(
                request.familyId(),
                request.hospitalId(),
                request.department(),
                request.date(),
                request.taken(),
                request.alarm(),
                request.symptom(),
                true
        );
    }

    public Treatment toEntity(Family family, Hospital hospital) {
        return Treatment.builder()
                .family(family)
                .hospital(hospital)
                .prescription(null)
                .department(department())
                .date(LocalDateTime.parse(date()))
                .taken(taken())
                .alarm(alarm())
                .symptom(symptom())
                .active(true)
                .build();
    }
}
