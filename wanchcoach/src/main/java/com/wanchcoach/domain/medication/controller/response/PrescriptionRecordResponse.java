package com.wanchcoach.domain.medication.controller.response;

import java.util.List;

public record PrescriptionRecordResponse(
        String familyColor,
        List<PrescriptionTakingRecord> taking,
        List<PrescriptionEndRecord> end

) {
}
