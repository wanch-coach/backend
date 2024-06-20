package com.wanchcoach.domain.family.controller;

import com.wanchcoach.domain.family.controller.request.FamilyAddRequest;
import com.wanchcoach.domain.family.controller.request.FamilyUpdateRequest;
import com.wanchcoach.domain.family.controller.response.FamiliesResponse;
import com.wanchcoach.domain.family.controller.response.FamilyInfoResponse;
import com.wanchcoach.domain.family.entity.Family;
import com.wanchcoach.domain.family.service.FamilyService;
import com.wanchcoach.domain.family.service.dto.FamilyAddDto;
import com.wanchcoach.domain.family.service.dto.FamilyUpdateDto;
import com.wanchcoach.global.api.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/family")
public class FamilyController {

    private final FamilyService familyService;

    @GetMapping
    public ApiResult<List<FamiliesResponse>> selectFamilies(@AuthenticationPrincipal User user){
        Long memberId = Long.valueOf(user.getUsername());
        log.info(String.valueOf(memberId));
        return familyService.selectFamilies(memberId);
    }
    @GetMapping("/{familyId}")
    public ApiResult<FamilyInfoResponse> selectFamily(@PathVariable("familyId") String familyId){
        return familyService.selectFamily(Long.valueOf(familyId));
    }

    @PostMapping
    public ApiResult<Family> addFamily(@RequestBody FamilyAddRequest familyAddRequest, @AuthenticationPrincipal User user){
        Long memberId = Long.valueOf(user.getUsername());
        return familyService.addFamily(FamilyAddDto.of(familyAddRequest, memberId));
    }
    @PatchMapping("/{familyId}")
    public ApiResult<FamilyInfoResponse> updateFamily(@PathVariable("familyId") String familyId,@RequestBody FamilyUpdateRequest familyUpdateRequest){
        familyUpdateRequest.setFamilyId(Long.valueOf(familyId));
        return familyService.updateFamily(FamilyUpdateDto.of(familyUpdateRequest));
    }

    @DeleteMapping("/{familyId}")
    public ApiResult<Void> deleteFamily(@PathVariable("familyId") String familyId){
        return familyService.deleteFamily(Long.valueOf(familyId));
    }

}
