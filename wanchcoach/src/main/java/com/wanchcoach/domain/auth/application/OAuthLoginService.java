package com.wanchcoach.domain.auth.application;

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
                .email(oAuthInfoResponse.getEmail())
                .gender(oAuthInfoResponse.getGender())
                .birthday(oAuthInfoResponse.getBirthday())
                .build();
        return memberRepository.save(member);
    }

}
