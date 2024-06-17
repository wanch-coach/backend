package com.wanchcoach.domain.member.controller;

import com.wanchcoach.domain.member.controller.request.MemberSignupRequest;
import com.wanchcoach.domain.member.entity.Member;
import com.wanchcoach.domain.member.service.MemberService;
import com.wanchcoach.domain.member.service.dto.MemberSignupDto;
import com.wanchcoach.global.api.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.model.Message;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

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
    public ApiResult<String> sendsms(@RequestParam("phoneNumber") String phoneNumber){
        return memberService.sendsms(phoneNumber);
    }

}
