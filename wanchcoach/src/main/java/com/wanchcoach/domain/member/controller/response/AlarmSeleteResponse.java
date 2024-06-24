package com.wanchcoach.domain.member.controller.response;

import com.wanchcoach.domain.member.entity.DrugAdministrationTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class AlarmSeleteResponse {

    LocalTime morning;
    LocalTime noon;
    LocalTime evening;
    LocalTime beforeBed;


    public static AlarmSeleteResponse of(DrugAdministrationTime drugAdministrationTime) {
        return new AlarmSeleteResponse(drugAdministrationTime.getMorning(),
                drugAdministrationTime.getNoon(),
                drugAdministrationTime.getEvening(),
                drugAdministrationTime.getBeforeBed());
    }

}
