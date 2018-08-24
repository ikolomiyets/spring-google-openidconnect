package com.github.fromi.openidconnect.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import static java.util.Arrays.asList;
import static org.springframework.security.oauth2.common.AuthenticationScheme.header;

@Configuration
@EnableOAuth2Client
public class OAuth2Client {
    @Value("${oauth2.clientId}")
    private String clientId;

    @Value("${oauth2.clientSecret}")
    private String clientSecret;

    @Value("${oauth2.authorizationUri}")
    private String authorizationUri;

    @Value("${oauth2.accessTokenUri}")
    private String accessTokenUri;

    @Value("${oauth2.redirectUri}")
    private String redirectUri;

    @Bean
    public OAuth2ProtectedResourceDetails googleOAuth2Details() {
        AuthorizationCodeResourceDetails googleOAuth2Details = new AuthorizationCodeResourceDetails();
        googleOAuth2Details.setClientId(clientId);
        googleOAuth2Details.setClientSecret(clientSecret);
        googleOAuth2Details.setUserAuthorizationUri(authorizationUri);
        googleOAuth2Details.setAccessTokenUri(accessTokenUri);
        googleOAuth2Details.setPreEstablishedRedirectUri(redirectUri);
        googleOAuth2Details.setUseCurrentUri(false);

        googleOAuth2Details.setAuthenticationScheme(header);
        googleOAuth2Details.setClientAuthenticationScheme(header);

        googleOAuth2Details.setScope(asList("openid"));
        return googleOAuth2Details;
    }

    @Bean
    public OAuth2RestTemplate googleOAuth2RestTemplate(OAuth2ClientContext clientContext) {
        return new OAuth2RestTemplate(googleOAuth2Details(), clientContext);
    }
}
