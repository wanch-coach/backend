package com.wanchcoach.domain.treatment.entity;

import com.wanchcoach.global.entity.BaseEntity;
import com.wanchcoach.domain.medical.entity.Pharmacy;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

/**
 * 처방전 엔티티
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
@Table(name = "prescription")
public class Prescription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long prescriptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private Pharmacy pharmacy;

    @Column(nullable = false)
    private Integer remains;

    @Column(nullable = false)
    private Boolean taking;

    @Column
    private LocalDate endDate;

    @Column(nullable = false)
    private Boolean morning;

    @Column(nullable = false)
    private Boolean noon;

    @Column(nullable = false)
    private Boolean evening;

    @Column(nullable = false)
    private Boolean beforeBed;

    @Column
    private String imageFileName;

    @Column
    private Boolean active;

    public void delete() {
        this.active = false;
    }

    public void update(Prescription prescription) {
        this.pharmacy = prescription.getPharmacy();
        this.morning = prescription.getMorning();
        this.noon = prescription.getNoon();
        this.evening = prescription.getEvening();
        this.beforeBed = prescription.getBeforeBed();
    }

    public void updateTakingAndEndDate(Boolean taking, LocalDate endDate) {
        this.taking = taking;
        this.endDate = endDate;
    }

    public void updateImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}
