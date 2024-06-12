package com.wanchcoach.app.domain.medical.entity;

import com.wanchcoach.app.domain.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalTime;

/**
 * 약국 영업 엔티티
 *
 * @author 박은규
 */

public class PharmacyOpeningHour extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pharmacy_oh_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private Pharmacy pharmacy;

    @Column(nullable = false)
    private Integer dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;
}
