package com.wanchcoach.domain.family.service;

import com.wanchcoach.domain.family.entity.Family;
import com.wanchcoach.domain.family.repository.command.FamilyRepository;
import com.wanchcoach.domain.family.service.dto.FamilyAddDto;
import com.wanchcoach.domain.member.entity.Member;
import com.wanchcoach.domain.member.repository.MemberRepository;
import com.wanchcoach.global.api.ApiResult;
import com.wanchcoach.global.error.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
