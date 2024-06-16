package com.wanchcoach.domain.auth.application;

import com.wanchcoach.domain.auth.apiClient.OAuthApiClient;
import com.wanchcoach.domain.auth.infoResponse.OAuthInfoResponse;
import com.wanchcoach.domain.auth.params.OAuthLoginParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RequestOAuthInfoService {

    private final Map<OAuthProvider, OAuthApiClient> clients;

    public RequestOAuthInfoService(List<OAuthApiClient> clients) {
        this.clients = clients.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toMap(OAuthApiClient::oAuthProvider, Function.identity()),
                        Collections::unmodifiableMap
                )
        );
    }
    public OAuthInfoResponse request(OAuthLoginParams params) {
        OAuthApiClient client = clients.get(params.oAuthProvider());
        log.info(params.toString());
        log.info(client.toString());
        String accessToken = client.requestAccessToken(params);
        System.out.println();
        return client.requestOauthInfo(accessToken);
    }
}
