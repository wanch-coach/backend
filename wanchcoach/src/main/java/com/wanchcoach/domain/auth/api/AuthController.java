package com.wanchcoach.domain.auth.api;

import com.wanchcoach.domain.auth.application.OAuthLoginService;
import com.wanchcoach.domain.auth.params.GoogleLoginParams;
import com.wanchcoach.domain.auth.params.NaverLoginParams;
import com.wanchcoach.domain.auth.tokens.AuthTokens;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final OAuthLoginService oAuthLoginService;

// https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=quIIXrYYRk1GoMBFxXNn&state=STATE_STRING&redirect_uri=http://localhost:8081/login/oauth2/code/naver
    @GetMapping("/login/oauth2/code/naver")
    public ResponseEntity<AuthTokens> loginNaver(NaverLoginParams params, HttpServletRequest request) {
        log.info(params.toString());
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }


//    https://accounts.google.com/o/oauth2/v2/auth?client_id=565955271344-7eprc3nfn0sveu7u8f19omb4et6toji3.apps.googleusercontent.com&redirect_uri=http://localhost:8081/login/oauth2/code/google&response_type=code&scope=email profile
    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<AuthTokens> loginGoogle(GoogleLoginParams params, HttpServletRequest request){
        log.info(params.toString());
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }


    @GetMapping("/test")
    public String test(){
        return "test";
    }
}
