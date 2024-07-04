package com.wanchcoach.domain.drug.service.dto;

import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchDrugsDetailDto {
    private Long favoriteId;//즐겨찾기 여부
    private Long drugId; //약 식별자
    private String itemName; //제품명
    private String itemEngName; //제품 영문명
    private String entpName; //업체명
    private String spcltyPblc; //약품 구분(전문 or 일반)
    private String prductType; //약품 타입
    private String itemIngrName; //약품 재료
    //여기부터 특화 상세 API 조회
    private String storageMethod; //보관 방법
    private String validTerm; //유통기한
    private String eeDocData; //효능 효과 DATA
    private String udDocData; //용법 용량 DATA
    private String nbDocData; //사용상의 주의사항 DATA
    private String filePath; //이미지 저장 경로


    public SearchDrugDetailResponse toSearchDrugDetailResponse(){

        if(!this.filePath.equals("")){

            String saveName = this.filePath;
            File file = new File(saveName);
            byte[] result = null;
            try {
                result = FileCopyUtils.copyToByteArray(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new SearchDrugDetailResponse(this.favoriteId, this.drugId, this.itemName, this.itemEngName, this.entpName, this.spcltyPblc, this.prductType, this.itemIngrName, this.storageMethod, this.validTerm, this.eeDocData, this.udDocData, this.nbDocData , Base64.getEncoder().encodeToString(result));
        }
        return new SearchDrugDetailResponse(this.favoriteId, this.drugId, this.itemName, this.itemEngName, this.entpName, this.spcltyPblc, this.prductType, this.itemIngrName, this.storageMethod, this.validTerm, this.eeDocData, this.udDocData, this.nbDocData ,null);
    }

}
