package com.wanchcoach.domain.medication.controller.response;

import java.util.List;

public record RecordCalendarDay(
        int day,
        List<RecordCalendarDayInfo> morning,
        List<RecordCalendarDayInfo> noon,
        List<RecordCalendarDayInfo> evening,
        List<RecordCalendarDayInfo> beforeBed

) {
}
