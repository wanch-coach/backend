package com.wanchcoach.domain.auth.params;

import com.wanchcoach.domain.auth.application.OAuthProvider;
import lombok.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class NaverLoginParams implements OAuthLoginParams {
    private String code;
    private String state;


    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.NAVER;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("state", state);
        return body;
    }
}