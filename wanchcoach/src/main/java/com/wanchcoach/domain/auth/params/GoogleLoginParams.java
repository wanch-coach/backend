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
public class GoogleLoginParams implements OAuthLoginParams{

    private String code;
    private String scope;
    private String authuser;
    private String prompt;


    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.GOOGLE;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("code", code);
        body.add("scope", scope);
        body.add("authuser", authuser);
        body.add("prompt", prompt);

        return body;
    }

    public String getCode(){
        return this.code;
    }
}
