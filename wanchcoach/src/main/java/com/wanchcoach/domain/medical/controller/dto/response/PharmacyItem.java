package com.wanchcoach.domain.medical.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PharmacyItem {
    Long pharmacyId;
    String name;
    String address;
}
