package com.wanchcoach.domain.family.service;

import com.wanchcoach.domain.family.controller.response.FamiliesResponse;
import com.wanchcoach.domain.family.controller.response.FamilyInfoResponse;
import com.wanchcoach.domain.family.entity.Family;
import com.wanchcoach.domain.family.repository.command.FamilyRepository;
import com.wanchcoach.domain.family.service.dto.FamilyAddDto;
import com.wanchcoach.domain.family.service.dto.FamilyUpdateDto;
import com.wanchcoach.domain.member.entity.Member;
import com.wanchcoach.domain.member.repository.MemberRepository;
import com.wanchcoach.global.api.ApiResult;
import com.wanchcoach.global.error.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final MemberRepository memberRepository;

    public ApiResult<Family> addFamily(FamilyAddDto familyAddDto) {
        Member member = memberRepository.findById(familyAddDto.memberId())
                .orElseThrow(() -> new NotFoundException(Member.class, familyAddDto.memberId()));
        return ApiResult.OK(familyRepository.save(familyAddDto.toEntity(member)));
    }

    public ApiResult<List<FamiliesResponse>> selectFamilies(Long memberId) {
        List<Family> families = familyRepository.findAllByMemberMemberId(memberId);
        List<FamiliesResponse> familiesRespons = families.stream().map(FamiliesResponse::from).collect(Collectors.toList());
        return ApiResult.OK(familiesRespons);
    }

    @Transactional
    public ApiResult<FamilyInfoResponse> updateFamily(FamilyUpdateDto familyUpdateDto) {
        Family family = familyRepository.findById(familyUpdateDto.familyId())
                .orElseThrow(() -> new NotFoundException(Family.class, familyUpdateDto.familyId()));
        family.update(familyUpdateDto);
        return ApiResult.OK(FamilyInfoResponse.from(family));
    }

    public ApiResult<FamilyInfoResponse> selectFamily(Long familyId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new NotFoundException(Family.class, familyId));
        return ApiResult.OK(FamilyInfoResponse.from(family));
    }
}
