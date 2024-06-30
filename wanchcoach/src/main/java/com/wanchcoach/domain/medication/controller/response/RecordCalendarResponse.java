package com.wanchcoach.domain.medication.controller.response;

import java.util.List;

public record RecordCalendarResponse(
    int year,
    int month,
    String color,
    List<RecordCalendarDay> records

) {
}
