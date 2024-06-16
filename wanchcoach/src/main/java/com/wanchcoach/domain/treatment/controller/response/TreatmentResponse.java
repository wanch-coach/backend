package com.wanchcoach.domain.treatment.controller.response;

import java.util.List;

public record TreatmentResponse(List<TreatmentItem> upcoming, List<TreatmentItem> past) {
}
