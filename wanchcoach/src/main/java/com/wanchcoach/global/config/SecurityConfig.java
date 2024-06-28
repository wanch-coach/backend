package com.wanchcoach.global.config;

import com.wanchcoach.domain.auth.tokens.JwtAuthenticationFilter;
import com.wanchcoach.domain.auth.tokens.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    private final OAuthLoginService oAuthLoginService;
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

//    public SecurityConfig(OAuthLoginService oAuthLoginService) {
//        this.oAuthLoginService = oAuthLoginService;
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf((csrf) -> csrf.disable());
//        http.formLogin((login) -> login.disable());
//        http.httpBasic((basic) -> basic.disable());

        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests((auth) -> auth.requestMatchers("/api/member/**","/login/**","/api/drug/*"
                        , "/api/treatment/**", "/api/medical/**", "/signin/**").permitAll()
                // 회원가입 관련
                // /api/member/signin
                // /api/member/signup
                // /api/member/findLoginId
                // /api/member/sendsms
                // /api/member/idcheck
                // /api/member/changepwdauth
                // /login/**
                .anyRequest().authenticated());

        return http.build();
    }

}
