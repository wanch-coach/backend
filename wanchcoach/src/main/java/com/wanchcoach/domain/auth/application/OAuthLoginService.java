package com.wanchcoach.domain.auth.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wanchcoach.domain.auth.infoResponse.OAuthInfoResponse;
import com.wanchcoach.domain.auth.params.OAuthLoginParams;
import com.wanchcoach.domain.auth.tokens.AuthTokenGenerator;
import com.wanchcoach.domain.auth.tokens.AuthTokens;
import com.wanchcoach.domain.member.entity.Member;
import com.wanchcoach.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthLoginService {

    private final MemberRepository memberRepository;
    private final AuthTokenGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;

    @Transactional
    public AuthTokens login(OAuthLoginParams params) {
        log.info("<<< login service >>> ");
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        log.info(oAuthInfoResponse.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            // 객체를 JSON 문자열로 변환
            String json = objectMapper.writeValueAsString(oAuthInfoResponse);
            System.out.println("test" + json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String loginId = oAuthInfoResponse.getOAuthProvider() + oAuthInfoResponse.getEmail();


        Member member = findOrCreateMember(oAuthInfoResponse, loginId);
        log.info(member.toString());

        AuthTokens authTokens = authTokensGenerator.generate(member.getMemberId());
        member.updateRefreshToken(authTokens.getRefreshToken());

        return authTokens;
    }

    private Member findOrCreateMember(OAuthInfoResponse oAuthInfoResponse, String loginId) {
        return memberRepository.findByLoginId(loginId).orElseGet(() -> newMember(oAuthInfoResponse, loginId));

    }

    private Member newMember(OAuthInfoResponse oAuthInfoResponse, String loginId){
        Member member = Member.builder()
                .loginId(loginId)
                .encryptedPwd("-----")
                .email(oAuthInfoResponse.getEmail())
                .name(oAuthInfoResponse.getName())
                .birthDate(oAuthInfoResponse.getBirthday())
                .gender(oAuthInfoResponse.getGender())
                .phoneNumber(oAuthInfoResponse.getMobile())
                .active(true) // 예시로 true로 설정
                .loginType(true) // 예시로 true로 설정
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .locationPermission(true) // 예시로 true로 설정
                .callPermission(true) // 예시로 true로 설정
                .cameraPermission(true) // 예시로 true로 설정
                .build();
        return memberRepository.save(member);
    }

}
