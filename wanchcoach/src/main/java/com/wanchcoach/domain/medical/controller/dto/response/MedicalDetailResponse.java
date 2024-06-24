package com.wanchcoach.domain.medical.controller.dto.response;

import java.util.List;

public record MedicalDetailResponse(List<HospitalDetailItem> hospitals, List<PharmacyDetailItem> pharmacies) {
}
