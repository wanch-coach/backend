package com.wanchcoach.domain.drug.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;


import com.wanchcoach.domain.drug.service.dto.SearchFavoritesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.wanchcoach.domain.drug.entity.QFavoriteDrug.favoriteDrug;
import static com.wanchcoach.domain.drug.entity.QDrug.drug;
import static com.wanchcoach.domain.drug.entity.QDrugImage.drugImage;

@Repository
@RequiredArgsConstructor
public class FavoriteDrugQRepository {


    private final JPAQueryFactory queryFactory;

    public List<SearchFavoritesDto> searhFavorites(Long memberId){
        // TODO: 2024-06-17 memebr Entity로 수정
        List<SearchFavoritesDto> favoriteList  = queryFactory.select(Projections.constructor(SearchFavoritesDto.class,
                        favoriteDrug.favoriteId,
                        drug.drugId,
                        drug.itemName,
                        drug.spcltyPblc,
                        drugImage.filePath.coalesce("")
                ))
                .from(favoriteDrug)
                .join(drug).on(favoriteDrug.drug.drugId.eq(drug.drugId))
                .leftJoin(drugImage).on(drug.drugImage.drug_image_id.eq(drugImage.drug_image_id))
                .where(favoriteDrug.member.eq(memberId))
                .fetch();
        return favoriteList;
    }
}
