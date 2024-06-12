package com.wanchcoach.app.domain.medical.entity;

import com.wanchcoach.app.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * 약국 엔티티
 *
 * @author 박은규
 */

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "pharmacy")
public class Pharmacy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pharmacy_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(precision = 15, scale = 12, nullable = false)
    private BigDecimal longitude;

    @Column(precision = 15, scale = 13, nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private String postCdn1;

    @Column(nullable = false)
    private String postCdn2;
}
