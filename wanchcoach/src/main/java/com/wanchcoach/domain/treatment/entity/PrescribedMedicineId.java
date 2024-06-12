package com.wanchcoach.domain.treatment.entity;

import lombok.*;

import java.io.Serializable;

@Getter
//@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PrescribedMedicineId implements Serializable {
    private Long prescription;
    private Long medicine;
}
