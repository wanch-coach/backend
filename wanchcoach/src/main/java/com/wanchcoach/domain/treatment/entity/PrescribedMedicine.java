package com.wanchcoach.domain.treatment.entity;

import com.wanchcoach.domain.BaseEntity;
import com.wanchcoach.domain.medicine.entity.Medicine;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * 처방받은 약 엔티티
 *
 * @author 박은규
 */

@Entity
@Getter
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "prescribed medicine")
@IdClass(PrescribedMedicineId.class)
public class PrescribedMedicine extends BaseEntity implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    // todo: 약 저장
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @Column
    private Double quantity;

    @Column
    private Integer frequency;

    @Column
    private Integer day;

    @Column
    private String direction;
}
