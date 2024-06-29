package com.wanchcoach.domain.drug.service;

import com.wanchcoach.domain.drug.entity.Drug;
import com.wanchcoach.domain.drug.entity.FavoriteDrug;
import com.wanchcoach.domain.drug.repository.DrugRepository;
import com.wanchcoach.domain.drug.repository.FavoriteDrugRepository;
import com.wanchcoach.domain.member.entity.Member;
import com.wanchcoach.domain.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    public void createFavorite(Long memberId, Long drugId){

        Member member = memberRepository.findByMemberId(memberId);

        Drug drug = drugRepository.findById(drugId).orElseThrow();

        FavoriteDrug favoriteDrug = FavoriteDrug.builder()
                                    .member(member)
                                    .drug(drug)
                                    .build();
        favoriteDrugRepository.save(favoriteDrug);
    }

    public void deleteFavorite(Long favoriteId){

        favoriteDrugRepository.deleteById(favoriteId);

    }
}
