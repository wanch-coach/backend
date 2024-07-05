package com.wanchcoach.domain.auth.controller;

import com.wanchcoach.domain.auth.application.OAuthLoginService;
import com.wanchcoach.domain.auth.controller.response.SocialResponse;
import com.wanchcoach.domain.auth.params.KaKaoLoginParams;
import com.wanchcoach.domain.auth.params.NaverLoginParams;
import com.wanchcoach.domain.auth.tokens.AuthTokens;
import com.wanchcoach.global.api.ApiResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class AuthController {

    private final OAuthLoginService oAuthLoginService;

// https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=quIIXrYYRk1GoMBFxXNn&state=STATE_STRING&redirect_uri=http://localhost:8081/api/login/oauth2/code/naver
    @PostMapping("/login/oauth2/code/naver")
    public ApiResult<?> loginNaver(@RequestBody NaverLoginParams params, HttpServletRequest request) {
        log.info(params.toString());
        return ApiResult.OK(oAuthLoginService.login(params));
    }

// https://kauth.kakao.com/oauth/authorize?client_id=370123a2ecc923df6371e651937c9038&redirect_uri=http://localhost:8081/api/login/oauth2/code/kakao&response_type=code&scope=account_email
    @GetMapping("/login/oauth2/code/kakao")
    public ApiResult<SocialResponse> loginKakao(KaKaoLoginParams params, HttpServletRequest request){
        log.info(params.toString());
        return ApiResult.OK(oAuthLoginService.login(params));
    }

}
