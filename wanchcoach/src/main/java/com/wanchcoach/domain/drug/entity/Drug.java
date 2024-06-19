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
public class Drug extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long drugId; //약 식별자

    @OneToOne
    @JoinColumn(name="drug_image_id")
    private DrugImage drugImage;

    @JoinColumn(name="item_seq")
    private Long itemSeq; //품목기준코드

    @Column(length=4000)
    @JoinColumn(name="item_name")
    private String itemName; //제품명

    @Column(length=4000)
    @JoinColumn(name="item_eng_name")
    private String itemEngName; //제품 영문명

    @Column(length=4000)
    @JoinColumn(name="entp_name")
    private String entpName; //업체명

    @Column(length=4000)
    @JoinColumn(name="entp_seq")
    private String entpSeq; //업체 식별자

    @Column(length=65535)
    @JoinColumn(name="item_permit_date")
    private String itemPermitDate; //특허 허가일 yyyymmdd

    @Column(length=65535)
    @JoinColumn(name="spclty_pblc")
    private String spcltyPblc; //약품 구분(전문 or 일반)

    @Column(length=65535)
    @JoinColumn(name="prduct_type")
    private String prductType; //약품 타입

    @Column(length=65535)
    @JoinColumn(name="item_ingr_name")
    private String itemIngrName; //약품 재료

    @Lob
    @JoinColumn(name="big_prdt_img_url")
    private String bigPrdtImgUrl; //약품 이미지

    //여기부터 특화 상세 API 조회
    @JoinColumn(name="storage_method")
    private String storageMethod; //보관 방법

    @JoinColumn(name="valid_term")
    private String validTerm; //유통기한

    @JoinColumn(name="change_date")
    private String changeDate; //정보 변경일 yyyymmdd

    @Lob
    @JoinColumn(name="ee_doc_data")
    private String eeDocData; //효능 효과 DATA

    @Lob
    @JoinColumn(name="ud_doc_data")
    private String udDocData; //용법 용량 DATA

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @JoinColumn(name="nb_doc_data")
    private String nbDocData; //사용상의 주의사항 DATA

    public void updateDrugDetail(String storageMethod, String validTerm, String changeDate, String eeDocData, String udDocData, String nbDocData){
        this.storageMethod = storageMethod;
        this.validTerm = validTerm;
        this.changeDate = changeDate;
        this.eeDocData = eeDocData;
        this.udDocData =udDocData;
        this.nbDocData = nbDocData;
    }
}
