package com.wanchcoach.domain.auth.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wanchcoach.domain.auth.controller.response.AuthSignupResponse;
import com.wanchcoach.domain.auth.controller.response.SocialResponse;
import com.wanchcoach.domain.auth.infoResponse.OAuthInfoResponse;
import com.wanchcoach.domain.auth.params.OAuthLoginParams;
import com.wanchcoach.domain.auth.tokens.AuthTokenGenerator;
import com.wanchcoach.domain.auth.tokens.AuthTokens;
import com.wanchcoach.domain.family.entity.Family;
import com.wanchcoach.domain.family.service.FamilyService;
import com.wanchcoach.domain.family.service.dto.FamilyAddDto;
import com.wanchcoach.domain.member.entity.Member;
import com.wanchcoach.domain.member.repository.MemberRepository;
import com.wanchcoach.domain.member.service.MemberService;
import com.wanchcoach.domain.member.service.dto.AlarmUpdateDto;
import com.wanchcoach.global.api.ApiResult;
import com.wanchcoach.global.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
/**
 * TODO
 * 회원가입 데이터 생성 시 Permission관련 데이터를 false로 생성하는 것이 아닌, default를 false로설정
 */
public class OAuthLoginService {

    private final MemberRepository memberRepository;
    private final AuthTokenGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final FamilyService familyService;
    private final MemberService memberService;


    public SocialResponse login(OAuthLoginParams params) {
        log.info("<<< login service >>> ");
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        log.info(oAuthInfoResponse.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            // 객체를 JSON 문자열로 변환
            String json = objectMapper.writeValueAsString(oAuthInfoResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String loginId = oAuthInfoResponse.getOAuthProvider() + oAuthInfoResponse.getEmail();

        if(memberRepository.existsByLoginId(loginId)){
            Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
            Member member = optionalMember.get();
            AuthTokens authTokens = authTokensGenerator.generate(member.getMemberId());
            member.updateRefreshToken(authTokens.getRefreshToken());

            return authTokens;
        }else{
            AuthSignupResponse authSignupResponse = AuthSignupResponse.of(oAuthInfoResponse);
            return authSignupResponse;
        }

    }

    private Family toEntity(Member member) {
        return Family.builder()
                .member(member)
                .name(member.getName())
                .birthDate(member.getBirthDate())
                .gender(member.getGender())
                .imageFileName(" ")
                .build();
    }

    private Member findOrCreateMember(OAuthInfoResponse oAuthInfoResponse, String loginId) {
        return memberRepository.findByLoginId(loginId).orElseGet(() -> newMember(oAuthInfoResponse, loginId));

    }

    private Member newMember(OAuthInfoResponse oAuthInfoResponse, String loginId){
        Member member = Member.builder()
                .loginId(loginId)
                .encryptedPwd(" ")
                .email(oAuthInfoResponse.getEmail())
                .name(oAuthInfoResponse.getName())
                .birthDate(oAuthInfoResponse.getBirthday())
                .gender(oAuthInfoResponse.getGender())
                .phoneNumber(oAuthInfoResponse.getMobile())
                .active(true) // 예시로 true로 설정
                .loginType(true) // 예시로 true로 설정
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .locationPermission(false) // 예시로 true로 설정
                .callPermission(false) // 예시로 true로 설정
                .cameraPermission(false) // 예시로 true로 설정
                .build();

        return memberRepository.save(member);
    }

}
