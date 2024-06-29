package com.wanchcoach.global.config;

import com.wanchcoach.domain.auth.tokens.JwtAuthenticationFilter;
import com.wanchcoach.domain.auth.tokens.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

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
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.formLogin((login) -> login.disable());
        http.httpBasic((basic) -> basic.disable());

        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests((auth) -> auth.requestMatchers("/api/member/**","/api/drug/*"
                        , "/api/treatment/**", "/api/medical/**","/api/family/**").permitAll()
                // 회원가입 관련
                // /api/member/signin
                // /api/member/signup
                // /api/member/findLoginId
                // /api/member/sendsms
                // /api/member/idcheck
                // /api/member/changepwdauth
                // /login/**
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest().authenticated());

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
//                Arrays.asList("http://localhost:3000",
//                "https://localhost:3000","https://dd64-222-112-228-17.ngrok-free.app",
//                "http://localhost:4040",
//                "https://192.168.1.62:3000",
//                "http://192.168.1.62:3000")
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTION"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "ngrok-skip-browser-warning"));
//        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
