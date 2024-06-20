package com.wanchcoach.domain.drug.service;

import com.wanchcoach.domain.drug.controller.dto.response.SearchFavoritesResponse;
import com.wanchcoach.domain.drug.repository.FavoriteDrugQRepository;
import com.wanchcoach.domain.drug.service.dto.SearchFavoritesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteDrugQService {

    private final FavoriteDrugQRepository favoriteDrugQRepository;

    public List<SearchFavoritesResponse> searchFavorites(Long memberId){

        List<SearchFavoritesDto> searchFavoritesList = favoriteDrugQRepository.searhFavorites(memberId);

        List<SearchFavoritesResponse>searchFavoritesResponses = new ArrayList<>();

        for(SearchFavoritesDto searchFavoritesDto:searchFavoritesList){
            searchFavoritesResponses.add(searchFavoritesDto.toSearchFavoritesResponse());
        }

        return searchFavoritesResponses;
    }

}
