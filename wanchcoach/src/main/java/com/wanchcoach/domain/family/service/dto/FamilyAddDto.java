package com.wanchcoach.domain.family.service.dto;

import com.wanchcoach.domain.family.controller.request.FamilyAddRequest;
import com.wanchcoach.domain.family.entity.Family;
import com.wanchcoach.domain.member.controller.request.MemberSignupRequest;
import com.wanchcoach.domain.member.entity.Member;

import java.time.LocalDate;

/**
 *
 * @param imageFileName
 * TO DO
 * filename을 어떤식으로 정할것인지 협의 필요
 */
public record FamilyAddDto (
        Long memberId,
        String name,
        LocalDate birthDate,
        String gender,
        String imageFileName,
        boolean type,
        String color
        ){

    public static FamilyAddDto of(MemberSignupRequest memberSignupRequest,Long memberId) {
        return new FamilyAddDto(memberId,
                memberSignupRequest.name(),
                memberSignupRequest.birthDate(),
                memberSignupRequest.gender(),
                "myImage",
                true,
                "#dddddd");
    }
    public static FamilyAddDto of(FamilyAddRequest familyAddRequest, Long memberId) {
        return new FamilyAddDto(
                memberId,
                familyAddRequest.name(),
                familyAddRequest.birthDate(),
                familyAddRequest.gender(),
                familyAddRequest.imageFileName(),
                false,
                familyAddRequest.color());
    }

    public static FamilyAddDto of(Member member) {
        return new FamilyAddDto(
                member.getMemberId(),
                member.getName(),
                member.getBirthDate(),
                member.getGender(),
                " ",
                true,
                "#dddddd"
        );
    }

    public Family toEntity(Member member){
        return Family.builder()
                .member(member)
                .name(this.name)
                .birthDate(this.birthDate)
                .gender(this.gender)
                .imageFileName(this.imageFileName)
                .type(this.type)
                .color(this.color)
                .build();
    }
}
