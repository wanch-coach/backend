package com.wanchcoach.domain.drug.service;

import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugDetailResponse;
import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugsResponse;
import com.wanchcoach.domain.drug.entity.Drug;
import com.wanchcoach.domain.drug.repository.DrugQRepository;
import com.wanchcoach.domain.drug.service.dto.SearchDrugsDetailDto;
import com.wanchcoach.domain.drug.service.dto.SearchDrugsDto;
import com.wanchcoach.domain.member.entity.Member;
import com.wanchcoach.global.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DrugQService {

    private final DrugQRepository drugQRepository;

    @Value("${data.drug-upload-link}")
    private String uploadLink;
    public List<SearchDrugsResponse> searchDrugs(String type, String keyword){

        List<SearchDrugsDto> drugInfo = drugQRepository.findDrugsContainKeyword(type, keyword);
        List<SearchDrugsResponse> drugList = new ArrayList<>();

        for(SearchDrugsDto searchDrugsDto:drugInfo){
            drugList.add(searchDrugsDto.toSearchDrugsResponse());
        }

        return drugList;
    }

    public SearchDrugDetailResponse searchDrugDetail(Long drugId){

        SearchDrugsDetailDto drugDetail = drugQRepository.findDrugDetail(drugId);
        if(drugDetail==null){
            throw new NotFoundException(Member.class, drugId);
        }
        SearchDrugDetailResponse searchDrugDetailResponse = drugDetail.toSearchDrugDetailResponse();

        return searchDrugDetailResponse;
    }

}
