package com.wanchcoach.domain.treatment.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TreatmentItem implements Comparable<TreatmentItem> {

    private Long id;
    private Long familyId;
    private String familyName;
    private String familyColor;
    private Long hospitalId;
    private String hospitalName;
    private Long prescriptionId;
    private String department;
    private LocalDateTime date;
    private Boolean taken;
    private Boolean alarm;
    private String symptom;

    @Override
    public int compareTo(TreatmentItem o) {
        return o.date.compareTo(this.date);
    }
}
