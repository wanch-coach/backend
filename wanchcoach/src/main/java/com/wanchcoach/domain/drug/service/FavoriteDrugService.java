package com.wanchcoach.domain.drug.service;

import com.wanchcoach.domain.drug.entity.Drug;
import com.wanchcoach.domain.drug.entity.FavoriteDrug;
import com.wanchcoach.domain.drug.repository.DrugRepository;
import com.wanchcoach.domain.drug.repository.FavoriteDrugRepository;
import com.wanchcoach.global.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteDrugService {

    private final FavoriteDrugRepository favoriteDrugRepository;
    private final DrugRepository drugRepository;

    public void createFavorite(Long memberId, Long drugId){

        // TODO: 2024-06-16 member Entity 주입
        Drug drug = drugRepository.findById(drugId).orElseThrow();

        FavoriteDrug favoriteDrug = FavoriteDrug.builder()
                                    .member(memberId)
                                    .drug(drug)
                                    .build();
        favoriteDrugRepository.save(favoriteDrug);
    }

    public void deleteFavorite(Long favoriteId){

        favoriteDrugRepository.deleteById(favoriteId);

    }
}
