package com.wanchcoach.domain.drug.service.dto;

import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugsResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchDrugsDto {
    private Long drugId;
    private String itemName;
    private String prductType;
    private String filePath;
    private Long favorite;

    public SearchDrugsResponse toSearchDrugsResponse(){

        if(!this.filePath.equals("")){

            String saveName = this.filePath;
            File file = new File(saveName);
            byte[] result = null;

            try {
                result = FileCopyUtils.copyToByteArray(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new SearchDrugsResponse(this.drugId, this.itemName, this.prductType, Base64.getEncoder().encodeToString(result), this.favorite);
        }
        return new SearchDrugsResponse(this.drugId, this.itemName, this.prductType, null, this.favorite);
    }
}
