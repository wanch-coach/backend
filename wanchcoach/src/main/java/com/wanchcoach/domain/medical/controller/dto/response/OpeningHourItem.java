package com.wanchcoach.domain.medical.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class OpeningHourItem {
    Integer dayOfWeek;
    LocalTime startTime;
    LocalTime endTime;
}
