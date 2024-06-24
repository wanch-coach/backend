package com.wanchcoach.domain.member.entity;

import com.wanchcoach.domain.member.service.dto.AlarmUpdateDto;
import com.wanchcoach.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "drug_administration_time")
public class DrugAdministrationTime extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private LocalTime morning;

    @Column(nullable = false)
    private LocalTime noon;

    @Column(nullable = false)
    private LocalTime evening;

    @Column(nullable = false)
    private LocalTime beforeBed;

    public void update(AlarmUpdateDto alarmUpdateDto) {
        this.morning = alarmUpdateDto.morning();
        this.noon = alarmUpdateDto.noon();
        this.evening = alarmUpdateDto.evening();
        this.beforeBed = alarmUpdateDto.beforeBed();
    }
}
