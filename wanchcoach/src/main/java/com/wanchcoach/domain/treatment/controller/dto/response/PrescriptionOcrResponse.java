package com.wanchcoach.domain.treatment.controller.dto.response;

import java.util.List;

public record PrescriptionOcrResponse(List<DrugOcrItem> ocrItems) {
}
