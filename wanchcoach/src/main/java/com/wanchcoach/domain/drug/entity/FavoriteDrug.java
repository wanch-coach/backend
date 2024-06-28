package com.wanchcoach.domain.drug.entity;

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
public class FavoriteDrug extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId;

    // TODO: 2024-06-16 멤버 구현 후 멤버로 수정
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="member_id")
//    private Member member;
    private Long member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="drug_id")
    private Drug drug;

}
