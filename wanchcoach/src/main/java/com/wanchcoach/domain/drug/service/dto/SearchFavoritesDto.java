package com.wanchcoach.domain.drug.service.dto;

import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugsResponse;
import com.wanchcoach.domain.drug.controller.dto.response.SearchFavoritesResponse;
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
public class SearchFavoritesDto {
    private Long favoriteId;
    private Long drugId;
    private String itemName;
    private String spcltyPblc;
    private String filePath;

    public SearchFavoritesResponse toSearchFavoritesResponse(){

        if(!this.filePath.equals("")){

            String saveName = this.filePath;
            File file = new File(saveName);
            byte[] result = null;

            try {
                result = FileCopyUtils.copyToByteArray(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new SearchFavoritesResponse(this.favoriteId, this.drugId, this.spcltyPblc, Base64.getEncoder().encodeToString(result));
        }
        return new SearchFavoritesResponse(this.favoriteId, this.drugId, this.spcltyPblc, null);
    }
}
