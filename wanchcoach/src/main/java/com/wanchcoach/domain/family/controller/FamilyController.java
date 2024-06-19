package com.wanchcoach.domain.family.controller;

import com.wanchcoach.domain.family.controller.request.FamilyAddRequest;
import com.wanchcoach.domain.family.entity.Family;
import com.wanchcoach.domain.family.service.FamilyService;
import com.wanchcoach.domain.family.service.dto.FamilyAddDto;
import com.wanchcoach.domain.member.entity.Member;
import com.wanchcoach.global.api.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/family")
public class FamilyController {

    private final FamilyService familyService;

    @GetMapping
    public void test(@AuthenticationPrincipal Member member){
        log.info(String.valueOf(member.getMemberId()));
    }
    @PostMapping
    public ApiResult<Family> addFamily(@RequestBody FamilyAddRequest familyAddRequest, @AuthenticationPrincipal User user){
        Long memberId = Long.valueOf(user.getUsername());
        return familyService.addFamily(FamilyAddDto.of(familyAddRequest, memberId));
    }

}