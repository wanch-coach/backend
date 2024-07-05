package com.wanchcoach.domain.auth.controller.response;

import com.wanchcoach.domain.auth.tokens.AuthTokens;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@Getter
@Setter
public class AuthSignupTokenResponse extends SocialResponse{

    boolean isSignUp;
    private String accessToken;
    private String refreshToken;
    private String grantType;
    private Long expiresIn;

    public AuthSignupTokenResponse(boolean isSignUp, String accessToken, String refreshToken, String grantType, Long expiresIn) {
        super("token");
        this.isSignUp = isSignUp;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.grantType = grantType;
        this.expiresIn = expiresIn;
    }

    public static AuthSignupTokenResponse of(AuthTokens authTokens) {
        return new AuthSignupTokenResponse(
                true,
                authTokens.getAccessToken(),
                authTokens.getRefreshToken(),
                authTokens.getGrantType(),
                authTokens.getExpiresIn()
        );
    }
}
