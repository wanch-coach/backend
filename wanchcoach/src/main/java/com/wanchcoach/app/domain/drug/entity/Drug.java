package com.wanchcoach.app.domain.drug.entity;

import com.wanchcoach.app.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Getter
@SuperBuilder
@DynamicInsert
public class Drug extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long drug_id; //약 식별자

    @JoinColumn(name="entp_name")
    private String entpName; //업체명

    @JoinColumn(name="item_name")
    private String itemName; //제품명

    @JoinColumn(name="item_seq")
    private String itemSeq; //품목기준코드

    @JoinColumn(name="efcy_qesitm")
    private String efcyQesitm; //효능

    @JoinColumn(name="use_method_qesitm")
    private String useMethodQesitm; //사용법

    @JoinColumn(name="atpn_warn_qesitm")
    private String atpnWarnQesitm; //주의사항경고

    @JoinColumn(name="atpn_qesitm")
    private String atpnQesitm; //주의사항

    @JoinColumn(name="intrc_qesitm")
    private String intrcQesitm; //상호작용

    @JoinColumn(name="se_qesitm")
    private String seQesitm; //부작용

    @JoinColumn(name="deposit_method_qesitm")
    private String depositMethodQesitm; //보관법

    @JoinColumn(name="open_de")
    private LocalDateTime openDe; //공개일자

    @JoinColumn(name="update_de")
    private LocalDateTime updateDe; //수정일자

    @JoinColumn(name="item_image")
    private String itemImage; //낱알이미지

}
