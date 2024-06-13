package com.wanchcoach.domain.treatment.entity;

import com.wanchcoach.global.entity.BaseEntity;
import com.wanchcoach.domain.medical.entity.Pharmacy;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * 처방전 엔티티
 *
 * @author 박은규
 */

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "prescription")
public class Prescription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private Pharmacy pharmacy;

    @Column(nullable = false)
    private Integer remains;

    @Column(nullable = false)
    private Boolean taking;

    @Column
    private LocalDate endDate;

    @Column
    private String imageFileName;

    public void updateImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}
