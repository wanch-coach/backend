package com.wanchcoach.domain.treatment.entity;

import com.wanchcoach.domain.drug.entity.Drug;
import com.wanchcoach.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 처방받은 약 엔티티
 *
 * @author 박은규
 */

@Entity
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "prescribed drug")
@IdClass(PrescribedDrugId.class)
public class PrescribedDrug extends BaseEntity implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    // todo: 약 저장
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drug_id", nullable = false)
    private Drug drug;

    @Column
    private Double quantity;

    @Column
    private Integer frequency;

    @Column
    private Integer day;

    @Column
    private String direction;
}
