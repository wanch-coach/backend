package com.wanchcoach.domain.treatment.entity;

import com.wanchcoach.domain.family.entity.Family;
import com.wanchcoach.global.entity.BaseEntity;
import com.wanchcoach.domain.medical.entity.Hospital;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 진료 엔티티
 *
 * @author 박은규
 */

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "treatment")
public class Treatment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatment_id")
    private Long id;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "family_id", nullable = false)
     private Family family;

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
