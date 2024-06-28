package com.wanchcoach.domain.member.service.dto;

import com.wanchcoach.domain.member.controller.request.AlarmUpdateRequest;
import com.wanchcoach.domain.member.entity.DrugAdministrationTime;
import com.wanchcoach.domain.member.entity.Member;

import java.time.LocalTime;

public record AlarmUpdateDto(
        Long memberId,
        LocalTime morning,
        LocalTime noon,
        LocalTime evening,
        LocalTime beforeBed
) {
    public static AlarmUpdateDto of(Long memberId, AlarmUpdateRequest alarmUpdateRequest){
        return new AlarmUpdateDto(
                memberId,
                alarmUpdateRequest.morning(),
                alarmUpdateRequest.noon(),
                alarmUpdateRequest.evening(),
                alarmUpdateRequest.beforeBed()
        );
    }
    public static DrugAdministrationTime toEntity(Member member, AlarmUpdateDto alarmUpdateDto){
        return DrugAdministrationTime.builder()
                .member(member)
                .morning(alarmUpdateDto.morning)
                .noon(alarmUpdateDto.noon)
                .evening(alarmUpdateDto.evening)
                .beforeBed(alarmUpdateDto.beforeBed)
                .build();
    }

    public static AlarmUpdateDto defaultAlarmOf(Long memberId) {
        return new AlarmUpdateDto(
                memberId,
                LocalTime.of(8, 0),
                LocalTime.of(12, 0),
                LocalTime.of(18, 0),
                LocalTime.of(23, 0)
        );
    }
}
