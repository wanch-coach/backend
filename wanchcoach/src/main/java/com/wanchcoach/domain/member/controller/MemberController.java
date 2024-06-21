package com.wanchcoach.domain.member.controller;

import com.wanchcoach.domain.auth.tokens.AuthTokens;
import com.wanchcoach.domain.member.controller.request.MemberLoginRequest;
import com.wanchcoach.domain.member.controller.request.MemberSignupRequest;
import com.wanchcoach.domain.member.controller.response.AlarmSeleteResponse;
import com.wanchcoach.domain.member.service.dto.MemberLoginDto;
import com.wanchcoach.domain.member.entity.Member;
import com.wanchcoach.domain.member.service.MemberService;
import com.wanchcoach.domain.member.service.dto.MemberSignupDto;
import com.wanchcoach.global.api.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.model.Message;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ApiResult<AuthTokens> login(@RequestBody MemberLoginRequest memberLoginRequest){
        log.info("login Controller");
        return memberService.login(MemberLoginDto.of(memberLoginRequest));
    }

    @PostMapping("/signup")
    public ApiResult<Member> signup(@RequestBody MemberSignupRequest memberSignupRequest){
        log.info("signupRequestController");
        log.info(memberSignupRequest.toString());

        return memberService.signup(MemberSignupDto.of(memberSignupRequest));
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

}
