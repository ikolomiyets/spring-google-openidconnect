package com.github.fromi.openidconnect.security;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Optional.empty;

public class OpenIDConnectAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Resource
    private OAuth2RestOperations restTemplate;

    @Value("${oauth2.userInfoUri:}")
    private String userInfoUri;

    @Autowired
    private SignatureVerifier signatureVerifier;

    @Autowired
    private OpenIdConfiguration config;

    protected OpenIDConnectAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        if (config != null) {
            userInfoUri = config.getUserinfoEndpointUri();
        }

        setAuthenticationManager(authentication -> authentication); // AbstractAuthenticationProcessingFilter requires an authentication manager.
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        OAuth2AccessToken aToken = restTemplate.getAccessToken();
        String accessToken = aToken.getValue();
        logger.info(accessToken);
        String idTokenString = (String) aToken.getAdditionalInformation().get("id_token");
        Jwt idToken = JwtHelper.decode(idTokenString);
        if (signatureVerifier != null) {
            idToken.verifySignature(signatureVerifier);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        UserInfo userInfo = mapper.readValue(idToken.getClaims(), UserInfo.class);

        request.getSession().setAttribute("session_state", request.getParameter("session_state"));
        request.getSession().setAttribute("code", request.getParameter("code"));
        request.getSession().setAttribute("state", request.getParameter("state"));
        return new PreAuthenticatedAuthenticationToken(userInfo, empty(), userInfo.getAuthorities());
    }
}
