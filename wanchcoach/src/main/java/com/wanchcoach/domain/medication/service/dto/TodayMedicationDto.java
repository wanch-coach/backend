package com.wanchcoach.domain.medication.service.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TodayMedicationDto{
    private Long familyId;
    private String familyName;
    private String hospitalName;
    private String department;
    private Boolean morning;
    private Boolean noon;
    private Boolean evening;
    private Boolean beforeBed;
}
