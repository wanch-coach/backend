package com.wanchcoach.domain.member.controller;

import com.wanchcoach.domain.auth.tokens.AuthTokens;
import com.wanchcoach.domain.family.service.FamilyService;
import com.wanchcoach.domain.family.service.dto.FamilyAddDto;
import com.wanchcoach.domain.member.controller.request.*;
import com.wanchcoach.domain.member.controller.response.*;
import com.wanchcoach.domain.member.service.dto.*;
import com.wanchcoach.domain.member.entity.Member;
import com.wanchcoach.domain.member.service.MemberService;
import com.wanchcoach.global.api.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PATCH;

/**
 * TO DO 비밀번호 현재와 다른지 검증 필요
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final FamilyService familyService;

    @PostMapping("/signin")
    public ApiResult<AuthTokens> login(@RequestBody MemberLoginRequest memberLoginRequest){
        log.info("login Controller");
        return memberService.login(MemberLoginDto.of(memberLoginRequest));
    }
    @GetMapping("/memberInfo")
    public ApiResult<MemberInfoResponse> getMemberInfo(@AuthenticationPrincipal User user){
        Long memberId = Long.valueOf(user.getUsername());
        return memberService.getMemberInfo(memberId);
    }
    @GetMapping("/changepwdauth")
    public ApiResult<AuthTokens> changePwdAuth(@RequestBody ChangePwdInfoRequest changePwdInfoRequest){
        ChangePwdInfoDto changePwdInfoDto = ChangePwdInfoDto.of(changePwdInfoRequest);
        return memberService.changePwdAuth(changePwdInfoDto);
    }
    @PatchMapping("/changepwdauth")
    public ApiResult<Void> changePwd(@RequestBody ChangePwdRequest changePwdRequest, @AuthenticationPrincipal User user){
        Long memberId = Long.valueOf(user.getUsername());
        ChangePwdDto changePwdDto = ChangePwdDto.of(memberId, changePwdRequest);
        memberService.changePwd(changePwdDto);
        return ApiResult.OK(null);
    }

    @PatchMapping("/memberInfo")
    public ApiResult<MemberInfoResponse> updateMemberInfo(@RequestBody MemberUpdateInfoRequest memberUpdateInfoRequest , @AuthenticationPrincipal User user){
        Long memberId = Long.valueOf(user.getUsername());
        MemberUpdateInfoDto memberUpdateInfoDto = MemberUpdateInfoDto.of(memberUpdateInfoRequest, memberId);
        return memberService.updateMemberInfo(memberUpdateInfoDto);
    }
    @PostMapping("/signup")
    public ApiResult<MemberInfoResponse> signup(@RequestBody MemberSignupRequest memberSignupRequest){
        log.info("signupRequestController");
        log.info(memberSignupRequest.toString());
        ApiResult<MemberInfoResponse> response = memberService.signup(MemberSignupDto.of(memberSignupRequest));

        Long memberId = response.getData().memberId();
        FamilyAddDto familyAddDto = FamilyAddDto.of(memberSignupRequest, memberId);
        familyService.addFamily(familyAddDto);

        memberService.addDefaultAlarm(AlarmUpdateDto.defaultAlarmOf(memberId));
        return response;
    }
    @GetMapping("/findLoginId")
    public ApiResult<FindIdResponse> getMemberLoginId(@RequestBody FindMemberLoginIdRequest findMemberLoginIdRequest){
        return memberService.findMemberId(FindMemberLoginIdDto.of(findMemberLoginIdRequest));
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

    @GetMapping("/alarm-permission")
    public ApiResult<AlarmPermissionResponse> selectAlarmPermission(@AuthenticationPrincipal User user){
        Long memberId = Long.valueOf(user.getUsername());
        return memberService.selectAlarmPermission(memberId);
    }
    @PostMapping("/alarm-permission")
    public ApiResult<AlarmPermissionResponse> updateAlarmPermission(@AuthenticationPrincipal User user) {
        Long memberId = Long.valueOf(user.getUsername());
        return memberService.updateAlarmPermission(memberId);
    }

    @PatchMapping("/update-device")
    public ApiResult<Void> updateDeviceToken(@RequestBody DeviceTokenRequest request,@AuthenticationPrincipal User user){
        Long memberId = Long.valueOf(user.getUsername());
        String deviceToken = request.deviceToken();
        return memberService.updateDeviceToken(memberId, deviceToken);
    }

}
