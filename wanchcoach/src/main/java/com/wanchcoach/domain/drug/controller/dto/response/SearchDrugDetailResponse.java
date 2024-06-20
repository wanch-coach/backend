package com.wanchcoach.domain.drug.controller.dto.response;

import com.wanchcoach.domain.drug.entity.DrugImage;
import jakarta.persistence.*;

public record SearchDrugDetailResponse(
        Long drugId, //약 식별자
        String itemName, //제품명
        String itemEngName, //제품 영문명
        String entpName, //업체명
        String spcltyPblc, //약품 구분(전문 or 일반)
        String prductType, //약품 타입
        String itemIngrName, //약품 재료
        //여기부터 특화 상세 API 조회
        String storageMethod, //보관 방법
        String validTerm, //유통기한
        String eeDocData, //효능 효과 DATA
        String udDocData, //용법 용량 DATA
        String nbDocData, //사용상의 주의사항 DATA
        String drugImage //약 이미지
    )
{
}
