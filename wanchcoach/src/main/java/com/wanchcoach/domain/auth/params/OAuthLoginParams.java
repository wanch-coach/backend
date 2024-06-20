package com.wanchcoach.domain.auth.params;

import com.wanchcoach.domain.auth.application.OAuthProvider;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {
    OAuthProvider oAuthProvider();
    MultiValueMap<String, String> makeBody();
}
