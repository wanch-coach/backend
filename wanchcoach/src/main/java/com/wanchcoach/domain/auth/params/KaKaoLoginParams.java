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
public class KaKaoLoginParams implements OAuthLoginParams{
    private String code;
    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        return body;
    }
}
