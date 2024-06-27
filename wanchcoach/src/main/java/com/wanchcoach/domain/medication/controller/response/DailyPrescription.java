package com.wanchcoach.domain.medication.controller.response;

import java.util.List;

public record DailyPrescription (
    List<DailyPrescriptionInfo> unTaken,
    List<DailyPrescriptionInfo> taken
){

}
