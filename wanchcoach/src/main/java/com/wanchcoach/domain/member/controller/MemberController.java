package com.wanchcoach.domain.member.controller;

import com.wanchcoach.domain.auth.tokens.AuthTokens;
import com.wanchcoach.domain.family.service.FamilyService;
import com.wanchcoach.domain.family.service.dto.FamilyAddDto;
import com.wanchcoach.domain.member.controller.request.AlarmUpdateRequest;
import com.wanchcoach.domain.member.controller.request.MemberLoginRequest;
import com.wanchcoach.domain.member.controller.request.MemberSignupRequest;
import com.wanchcoach.domain.member.controller.response.AlarmSeleteResponse;
import com.wanchcoach.domain.member.controller.response.CallPermissionResponse;
import com.wanchcoach.domain.member.controller.response.CameraPermissionResponse;
import com.wanchcoach.domain.member.controller.response.LocationPermissionResponse;
import com.wanchcoach.domain.member.service.dto.AlarmUpdateDto;
import com.wanchcoach.domain.member.service.dto.MemberLoginDto;
import com.wanchcoach.domain.member.entity.Member;
import com.wanchcoach.domain.member.service.MemberService;
import com.wanchcoach.domain.member.service.dto.MemberSignupDto;
import com.wanchcoach.global.api.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final FamilyService familyService;

    @PostMapping("/login")
    public ApiResult<AuthTokens> login(@RequestBody MemberLoginRequest memberLoginRequest){
        log.info("login Controller");
        return memberService.login(MemberLoginDto.of(memberLoginRequest));
    }

    @PostMapping("/signup")
    public ApiResult<Member> signup(@RequestBody MemberSignupRequest memberSignupRequest){
        log.info("signupRequestController");
        log.info(memberSignupRequest.toString());
        ApiResult<Member> response = memberService.signup(MemberSignupDto.of(memberSignupRequest));

        Long memberId = response.getData().getMemberId();
        FamilyAddDto familyAddDto = FamilyAddDto.of(memberSignupRequest, memberId);
        familyService.addFamily(familyAddDto);

        memberService.addDefaultAlarm(AlarmUpdateDto.defaultAlarmOf(memberId));
        return response;
    }
    @PostMapping("/leave")
    public ApiResult<Void> leaveMember(@AuthenticationPrincipal User user){
        Long memberId = Long.valueOf(user.getUsername());
        return memberService.leaveMember(memberId);
    }

    @GetMapping("/idcheck/{id}")
    public ApiResult<Boolean> idDuplicateCheck(@PathVariable("id") String id){
        log.info("idDuplicateCheck Controller");
        log.info(id);
        return memberService.idDuplicateCheck(id);
    }

    @GetMapping("/sendsms")
    public ApiResult<String> sendSMS(@RequestParam("phoneNumber") String phoneNumber){
        return memberService.sendSMS(phoneNumber);
    }

    @GetMapping("/alarm")
    public ApiResult<AlarmSeleteResponse> selectAlarm(@AuthenticationPrincipal User user){
        Long memberId = Long.valueOf(user.getUsername());
        return memberService.selectAlarm(memberId);
    }

    @PutMapping("/alarm")
    public ApiResult<AlarmSeleteResponse> updateAlarm(@RequestBody AlarmUpdateRequest alarmUpdateRequest, @AuthenticationPrincipal User user){
        Long memberId = Long.valueOf(user.getUsername());
        return memberService.updateAlarm(AlarmUpdateDto.of(memberId, alarmUpdateRequest));
    }

    @GetMapping("/location-permission")
    public ApiResult<LocationPermissionResponse> selectLocationPermission(@AuthenticationPrincipal User user){
        Long memberId = Long.valueOf(user.getUsername());
        return memberService.selectLocationPermission(memberId);
    }

    @PostMapping("/location-permission")
    public ApiResult<LocationPermissionResponse> updateLocationPermission(@AuthenticationPrincipal User user){
        Long memberId = Long.valueOf(user.getUsername());
        return memberService.updateLocationPermission(memberId);
    }

    @GetMapping("/call-permission")
    public ApiResult<CallPermissionResponse> selectCallPermission(@AuthenticationPrincipal User user){
        Long memberId = Long.valueOf(user.getUsername());
        return memberService.selectCallPermission(memberId);
    }

    @PostMapping("/call-permission")
    public ApiResult<CallPermissionResponse> updateCallPermission(@AuthenticationPrincipal User user){
        Long memberId = Long.valueOf(user.getUsername());
        return memberService.updateCallPermission(memberId);
    }

    @GetMapping("/camera-permission")
    public ApiResult<CameraPermissionResponse> selectCameraPermission(@AuthenticationPrincipal User user){
        Long memberId = Long.valueOf(user.getUsername());
        return memberService.selectCameraPermission(memberId);
    }

    @PostMapping("/camera-permission")
    public ApiResult<CameraPermissionResponse> updateCameraPermission(@AuthenticationPrincipal User user){
        Long memberId = Long.valueOf(user.getUsername());
        return memberService.updateCameraPermission(memberId);
    }
}
