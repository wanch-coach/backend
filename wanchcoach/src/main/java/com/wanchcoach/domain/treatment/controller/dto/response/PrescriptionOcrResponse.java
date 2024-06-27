package com.wanchcoach.domain.treatment.controller.dto.response;

import com.wanchcoach.domain.treatment.service.dto.DrugOcrDto;

import java.util.List;

public record PrescriptionOcrResponse(List<DrugOcrDto> ocrItems) {
}
