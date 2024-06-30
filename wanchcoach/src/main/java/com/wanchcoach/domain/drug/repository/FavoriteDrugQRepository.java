package com.wanchcoach.domain.drug.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;


import com.wanchcoach.domain.drug.service.dto.SearchFavoritesDto;
import com.wanchcoach.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.wanchcoach.domain.drug.entity.QFavoriteDrug.favoriteDrug;
import static com.wanchcoach.domain.drug.entity.QDrug.drug;
import static com.wanchcoach.domain.drug.entity.QDrugImage.drugImage;
import static com.wanchcoach.domain.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class FavoriteDrugQRepository {


    private final JPAQueryFactory queryFactory;

    public List<SearchFavoritesDto> searhFavorites(Long memberId){

        List<SearchFavoritesDto> favoriteList  = queryFactory.select(Projections.constructor(SearchFavoritesDto.class,
                        favoriteDrug.favoriteId,
                        drug.drugId,
                        drug.itemName,
                        drug.spcltyPblc,
                        drugImage.filePath.coalesce("")
                ))
                .from(favoriteDrug)
                .join(drug).on(favoriteDrug.drug.drugId.eq(drug.drugId))
                .leftJoin(drugImage).on(drug.drugImage.drugImageId.eq(drugImage.drugImageId))
                .join(member).on(member.memberId.eq(favoriteDrug.member.memberId))
                .where(member.memberId.eq(memberId))
                .fetch();
        return favoriteList;
    }
}
