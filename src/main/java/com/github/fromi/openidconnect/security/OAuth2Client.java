package com.github.fromi.openidconnect.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.Base64;

import static java.util.Arrays.asList;
import static org.springframework.security.oauth2.common.AuthenticationScheme.header;

@Configuration
@EnableOAuth2Client
public class OAuth2Client {
    @Value("${oauth2.clientId:}")
    private String clientId;

    @Value("${oauth2.clientSecret:}")
    private String clientSecret;

    @Value("${oauth2.configurationUri:}")
    private String configurationUri;

    @Value("${oauth2.authorizationUri:}")
    private String authorizationUri;

    @Value("${oauth2.accessTokenUri:}")
    private String accessTokenUri;

    @Value("${oauth2.jwksUri:}")
    private String jwksUri;

    @Value("${oauth2.redirectUri:}")
    private String redirectUri;

    @Bean
    public OAuth2ProtectedResourceDetails googleOAuth2Details() {
        OpenIdConfiguration config = getOpenIdConfiguration();
        if (config != null) {
            authorizationUri = config.getAuthorizationEndpointUri();
            accessTokenUri = config.getTokenEndpointUri();
            jwksUri = config.getJwksUri();
        }

        AuthorizationCodeResourceDetails googleOAuth2Details = new AuthorizationCodeResourceDetails();
        googleOAuth2Details.setClientId(clientId);
        googleOAuth2Details.setClientSecret(clientSecret);
        googleOAuth2Details.setUserAuthorizationUri(authorizationUri);
        googleOAuth2Details.setAccessTokenUri(accessTokenUri);
        googleOAuth2Details.setPreEstablishedRedirectUri(redirectUri);
        googleOAuth2Details.setUseCurrentUri(false);

        googleOAuth2Details.setAuthenticationScheme(header);
        googleOAuth2Details.setClientAuthenticationScheme(header);

        googleOAuth2Details.setScope(asList("openid profile"));
        return googleOAuth2Details;
    }

    @Bean
    public OAuth2RestTemplate googleOAuth2RestTemplate(OAuth2ClientContext clientContext) {
        return new OAuth2RestTemplate(googleOAuth2Details(), clientContext);
    }

    @Bean
    public SignatureVerifier signatureVerifier() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Keys> keys = restTemplate.getForEntity(jwksUri, Keys.class);
        Key rsaKey = null;
        if (keys.getStatusCode() == HttpStatus.OK) {
            for (Key key : keys.getBody().getKeys()) {
                if (key.getKty().equalsIgnoreCase("RSA")) {
                    rsaKey = key;
                    break;
                }
            }
        }

        if (rsaKey != null) {
            BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(rsaKey.getN()));
            BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(rsaKey.getE()));
            return new RsaVerifier(modulus, exponent);
        }

        return null;
    }

    @Bean
    public OpenIdConfiguration getOpenIdConfiguration() {
        if (configurationUri != null) {
            RestTemplate template = new RestTemplate();
            ResponseEntity<OpenIdConfiguration> configResponse = template.getForEntity(configurationUri, OpenIdConfiguration.class);
            if (configResponse.getStatusCode() == HttpStatus.OK) {
                return configResponse.getBody();
            }
        }

        return null;
    }
}
