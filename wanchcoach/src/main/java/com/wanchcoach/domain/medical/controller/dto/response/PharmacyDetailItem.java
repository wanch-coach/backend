package com.wanchcoach.domain.medical.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class PharmacyDetailItem {
    private Long pharmacyId;
    private String name;
    private String address;
    private String phoneNumber;
    private BigDecimal distance;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String etc;
    private List<OpeningHourItem> openingHourItems;
}
