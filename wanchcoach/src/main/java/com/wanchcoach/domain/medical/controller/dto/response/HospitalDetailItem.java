package com.wanchcoach.domain.medical.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class HospitalDetailItem {
    private Long hospitalId;
    private String name;
    private String type;
    private String address;
    private String phoneNumber;
    private BigDecimal distance;
    private Integer hasEmergencyRoom;
    private String etc;
    private List<OpeningHourItem> openingHourItems;
}
