package com.wanchcoach.domain.family.entity;

import com.wanchcoach.domain.family.service.dto.FamilyUpdateDto;
import com.wanchcoach.domain.member.entity.Member;
import com.wanchcoach.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * 가족 엔티티
 *
 * @author 박은규
 */

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "family")
public class Family extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long familyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String gender;

    @Column
    private String imageFileName;

    public void update(FamilyUpdateDto familyUpdateDto) {
        this.name = familyUpdateDto.name();
        this.birthDate = familyUpdateDto.birthDate();
        this.gender = familyUpdateDto.gender();
        this.imageFileName = familyUpdateDto.imageFileName();
    }
}
