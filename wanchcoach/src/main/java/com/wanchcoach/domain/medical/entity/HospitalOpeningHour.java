package com.wanchcoach.domain.medical.entity;

import com.wanchcoach.domain.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalTime;

/**
 * 병의원 영업정보 엔티티
 *
 * @author 박은규
 */

public class HospitalOpeningHour extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_oh_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Column(nullable = false)
    private Integer dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;
}
