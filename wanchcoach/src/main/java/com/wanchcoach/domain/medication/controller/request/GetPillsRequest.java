package com.wanchcoach.domain.medication.controller.request;

import java.time.LocalDate;

public record GetPillsRequest(

        LocalDate startDate,
        LocalDate endDate
) {
}
