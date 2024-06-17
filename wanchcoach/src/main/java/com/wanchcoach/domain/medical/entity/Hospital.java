package com.wanchcoach.domain.medical.entity;

import com.wanchcoach.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * 병의원 엔티티
 *
 * @author 박은규
 */

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "hospital")
public class Hospital extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long hospitalId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(precision = 15, scale = 12, nullable = false)
    private BigDecimal longitude;

    @Column(precision = 15, scale = 13, nullable = false)
    private BigDecimal latitude;

    @Column(nullable = false)
    private String postCdn;
}