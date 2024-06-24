package com.wanchcoach.domain.medication.controller.response;

import com.wanchcoach.domain.medication.service.dto.TodayMedicationDto;

import java.util.List;

public record TodayMedicationResponse (
    List<TodayMedicationDto> morning,
    List<TodayMedicationDto> noon,
    List<TodayMedicationDto> evening,
    List<TodayMedicationDto> beforeBed

){

}
