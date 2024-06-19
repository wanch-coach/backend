package com.wanchcoach.domain.family.service.dto;

import com.wanchcoach.domain.family.controller.request.FamilyAddRequest;
import com.wanchcoach.domain.family.entity.Family;
import com.wanchcoach.domain.member.entity.Member;

import java.time.LocalDate;

public record FamilyAddDto (
        Long memberId,
        String name,
        LocalDate birthDate,
        String gender,
        String imageFileName
        ){

    public static FamilyAddDto of(FamilyAddRequest familyAddRequest, Long memberId) {
        return new FamilyAddDto(
                memberId,
                familyAddRequest.name(),
                familyAddRequest.birthDate(),
                familyAddRequest.gender(),
                familyAddRequest.imageFileName());
    }
    public Family toEntity(Member member){
        return Family.builder()
                .member(member)
                .name(this.name)
                .birthDate(this.birthDate)
                .gender(this.gender)
                .imageFileName(this.imageFileName)
                .build();
    }
}
