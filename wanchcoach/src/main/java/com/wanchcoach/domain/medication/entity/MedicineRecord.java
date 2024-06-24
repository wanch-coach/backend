package com.wanchcoach.domain.medication.entity;

import com.wanchcoach.domain.family.entity.Family;
import com.wanchcoach.domain.treatment.entity.Prescription;
import com.wanchcoach.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

@NoArgsConstructor
@Entity
@Getter
@SuperBuilder
@DynamicInsert
@ToString
public class MedicineRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicineRecordId;

    @ManyToOne
    @JoinColumn(name="family_id")
    private Family family;

    @ManyToOne
    @JoinColumn(name="prescription_id")
    private Prescription prescription;

    @Column(name="time", nullable = false)
    private int time;
}
