package com.wanchcoach.domain.treatment.entity;

import com.wanchcoach.domain.family.entity.Family;
import com.wanchcoach.global.entity.BaseEntity;
import com.wanchcoach.domain.medical.entity.Hospital;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

/**
 * 진료 엔티티
 *
 * @author 박은규
 */

@Entity
@Getter
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "treatment")
public class Treatment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long treatmentId;

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

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private Boolean taken;

    @Column(nullable = false)
    private Boolean alarm;

    @Column
    private String symptom;

    @Column
    private Boolean active;

    public void update(Treatment treatment) {
        this.family = treatment.getFamily();
        this.hospital = treatment.getHospital();
        this.department = treatment.getDepartment();
        this.date = treatment.getDate();
        this.taken = treatment.getTaken();
        this.alarm = treatment.getAlarm();
        this.symptom = treatment.getSymptom();
    }

    public Boolean updateTaken() {
        this.taken = !this.taken;
        return this.taken;
    }

    public Boolean updateAlarm() {
        this.alarm = !this.alarm;
        return this.alarm;
    }

    public void delete() {
        this.active = false;
    }

    public void updatePrescription(Prescription prescription) {
        this.prescription = prescription;
    }
}
