package com.wanchcoach.domain.medication.controller.response;

import java.util.List;

public record PrescriptionRecordResponse(
        List<PrescriptionTakingRecord> taking,
        List<PrescriptionEndRecord> end

) {
}
