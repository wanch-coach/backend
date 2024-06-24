package com.wanchcoach.domain.member.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

public record AlarmUpdateRequest(

        LocalTime morning,
        LocalTime noon,
        LocalTime evening,
        LocalTime beforeBed
) {
}
