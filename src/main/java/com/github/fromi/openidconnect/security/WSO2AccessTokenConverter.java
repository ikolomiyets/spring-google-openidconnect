package com.github.fromi.openidconnect.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WSO2AccessTokenConverter extends DefaultAccessTokenConverter {
    protected final Log logger = LogFactory.getLog(getClass());

    private RestOperations restTemplate;

    private String userInfoUri;

    private String accessToken;

    public WSO2AccessTokenConverter() {
        super();
        restTemplate = new RestTemplate();
        ((RestTemplate) restTemplate).setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            // Ignore 400
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400) {
                    super.handleError(response);
                }
            }
        });
    }

    public void setUserInfoUri(String userInfoUri) {
        this.userInfoUri = userInfoUri;
    }

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        logger.info(String.format("Extracting authentication from %s", map));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", this.accessToken));
        final ResponseEntity<UserInfo> userInfoResponseEntity = restTemplate.exchange(userInfoUri, HttpMethod.GET,
                new HttpEntity<>(headers), UserInfo.class);
//        authentication.setDetails(userInfoResponseEntity.getBody());

        List<String> authorities = userInfoResponseEntity.getBody().getAuthorities().stream().map(authority -> authority.getAuthority()).collect(Collectors.toList());

        ((Map<String, Object>)map).put(AUTHORITIES,  authorities);
        return super.extractAuthentication(map);
    }

    public OAuth2Authentication extractAuthentication(Map<String, ?> map, String accessToken) {
        this.accessToken = accessToken;
        return extractAuthentication(map);
    }
}
