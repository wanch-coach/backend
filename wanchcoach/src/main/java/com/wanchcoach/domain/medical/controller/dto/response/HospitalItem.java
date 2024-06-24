package com.wanchcoach.domain.medical.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HospitalItem {
    Long hospitalId;
    String name;
    String type;
    String address;
}
