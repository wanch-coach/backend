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

        http.authorizeHttpRequests((auth) -> auth.requestMatchers("/api/member/**","/login/**","/api/drug/*", "/api/treatment/**", "/api/medical/**").permitAll()
                .anyRequest().authenticated());

        return http.build();
    }

    //    private final OAuth2UserService oAuth2UserService;
//    private final CustomSuccessHandler customSuccessHandler;
//    private final JWTUtil jwtUtil;
//
//    public SecurityConfig(OAuth2UserService oAuth2UserService, CustomSuccessHandler customSuccessHandler, JWTUtil jwtUtil) {
//
//        this.oAuth2UserService = oAuth2UserService;
//        this.customSuccessHandler = customSuccessHandler;
//        this.jwtUtil = jwtUtil;
//    }
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//
//        http.csrf((csrf) -> csrf.disable());
//        http.formLogin((login) -> login.disable());
//        http.httpBasic((basic) -> basic.disable());
//
//        http.addFilterAfter(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
//
//        http.oauth2Login((oauth2) -> oauth2
//                .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
//                        .userService(oAuth2UserService))
//                .successHandler(customSuccessHandler));
//
//        http.addFilterBefore(new CustomLogoutFilter(jwtUtil), LogoutFilter.class);
////        http.oauth2Login(Customizer.withDefaults());
//
////        http.oauth2Login((oauth2) -> oauth2
//////                .loginPage("/login.html")
////                .userInfoEndpoint((userInfoEndpointConfig ->
////                        userInfoEndpointConfig.userService(oAuth2UserService))));
//
////        http.authorizeHttpRequests((auth) -> auth.requestMatchers("/","/oauth/**", "login/**").permitAll()
////                .anyRequest().authenticated());
//
//        http.authorizeHttpRequests((auth) -> auth.requestMatchers("/").permitAll()
//                .anyRequest().authenticated());
//
//        http.sessionManagement((session) -> session
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        return http.build();
//    }
}
