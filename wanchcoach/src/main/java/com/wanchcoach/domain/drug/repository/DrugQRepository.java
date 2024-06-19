package com.wanchcoach.domain.drug.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.wanchcoach.domain.drug.service.dto.SearchDrugsDetailDto;
import com.wanchcoach.domain.drug.service.dto.SearchDrugsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.wanchcoach.domain.drug.entity.QDrug.drug;
import static com.wanchcoach.domain.drug.entity.QDrugImage.drugImage;

@Repository
@RequiredArgsConstructor
public class DrugQRepository {

    private final JPAQueryFactory queryFactory;

    public List<SearchDrugsDto> findDrugsContainKeyword(String type, String keyword){

        System.out.println(type+" "+keyword);

        if(type.equals("itemName")){
            System.out.println("itemName");
            List<SearchDrugsDto> drugList  = queryFactory.select(Projections.constructor(SearchDrugsDto.class,
                            drug.drugId,
                            drug.itemName,
                            drug.spcltyPblc,
                            drugImage.filePath.coalesce("")
                    ))
                    .from(drug)
                    .leftJoin(drugImage).on(drug.drugImage.drug_image_id.eq(drugImage.drug_image_id))
                    .where(drug.itemName.contains(keyword))
                    .fetch();
            System.out.println(drugList.size());
            return drugList;
        }else if(type.equals("entpName")){
            List<SearchDrugsDto> drugList  = queryFactory.select(Projections.constructor(SearchDrugsDto.class,
                            drug.drugId,
                            drug.itemName,
                            drug.spcltyPblc,
                            drugImage.filePath.coalesce("")
                    ))
                    .from(drug)
                    .leftJoin(drugImage).on(drug.drugImage.drug_image_id.eq(drugImage.drug_image_id))
                    .where(drug.entpName.contains(keyword))
                    .fetch();
            System.out.println(drugList.toString());
            return drugList;
        }else{
            List<SearchDrugsDto> drugList = queryFactory.select(Projections.constructor(SearchDrugsDto.class,
                            drug.drugId,
                            drug.itemName,
                            drug.spcltyPblc,
                            drugImage.filePath.coalesce("")
                    ))
                    .from(drug)
                    .leftJoin(drugImage).on(drug.drugImage.drug_image_id.eq(drugImage.drug_image_id))
                    .where(drug.eeDocData.contains(keyword))
                    .fetch();
            System.out.println(drugList.toString());
            return drugList;
        }

    }

    public SearchDrugsDetailDto findDrugDetail(Long drugId){

        SearchDrugsDetailDto searchDrugDetailResponse = queryFactory.select(Projections.constructor(SearchDrugsDetailDto.class,
                        drug.drugId,
                        drug.itemName,
                        drug.itemEngName,
                        drug.entpName,
                        drug.spcltyPblc,
                        drug.prductType,
                        drug.itemIngrName,
                        drug.storageMethod,
                        drug.validTerm,
                        drug.eeDocData,
                        drug.udDocData,
                        drug.nbDocData,
                        drugImage.filePath.coalesce("")
                ))
                .from(drug)
                .leftJoin(drugImage).on(drug.drugImage.drug_image_id.eq(drugImage.drug_image_id))
                .where(drug.drugId.eq(drugId))
                .fetchFirst();
        System.out.println(searchDrugDetailResponse.toString());
        return searchDrugDetailResponse;
    }
}