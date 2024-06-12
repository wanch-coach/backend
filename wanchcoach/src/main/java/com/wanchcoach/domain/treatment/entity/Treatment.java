package com.wanchcoach.domain.treatment.entity;

import com.wanchcoach.domain.BaseEntity;
import com.wanchcoach.domain.medical.entity.Hospital;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 진료 엔티티
 *
 * @author 박은규
 */

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "treatment")
public class Treatment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatment_id")
    private Long id;

    // todo: Family entity 가져오기
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "family_id", nullable = false)
    // private Family family;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @ManyToOne
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;

    @Column
    private String department;

    @Column
    private LocalDateTime date;

    @Column
    private Boolean taken;

    @Column
    private Boolean alarm;

    @Column
    private String symptom;

    public void updatePrescription(Prescription prescription) {
        this.prescription = prescription;
    }
}
