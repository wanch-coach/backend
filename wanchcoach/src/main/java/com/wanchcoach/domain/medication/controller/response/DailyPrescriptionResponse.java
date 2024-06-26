package com.wanchcoach.domain.medication.controller.response;

import java.util.List;

public record DailyPrescriptionResponse(
        Long familyId,
        DailyPrescription morning,
        DailyPrescription noon,
        DailyPrescription evening,
        DailyPrescription beforeBed
) {
}
