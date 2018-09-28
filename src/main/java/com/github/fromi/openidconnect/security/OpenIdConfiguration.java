package com.github.fromi.openidconnect.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenIdConfiguration {
    @JsonProperty("scopes_supported")
    private String[] scopesSupported;
    @JsonProperty("check_session_iframe")
    private String checkSessionIframeUri;
    private String issuer;
    @JsonProperty("authorization_endpoint")
    private String authorizationEndpointUri;
    @JsonProperty("claims_supported")
    private String[] claimsSupported;
    @JsonProperty("token_endpoint")
    private String tokenEndpointUri;
    @JsonProperty("response_types_supported")
    private String[] responseTypesSupported;
    @JsonProperty("end_session_endpoint")
    private String endSssionEndpoint;
    @JsonProperty("userinfo_endpoint")
    private String userinfoEndpointUri;
    @JsonProperty("jwks_uri")
    private String jwksUri;
    @JsonProperty("subject_types_supported")
    private String[] subjectTypesSupported;
    @JsonProperty("id_token_signing_alg_values_supported")
    private String[] idTokenSigningAlgSupported;
    @JsonProperty("registration_endpoint")
    private String registrationEndpointUri;

    public String[] getScopesSupported() {
        return scopesSupported;
    }

    public void setScopesSupported(String[] scopesSupported) {
        this.scopesSupported = scopesSupported;
    }

    public String getCheckSessionIframeUri() {
        return checkSessionIframeUri;
    }

    public void setCheckSessionIframeUri(String checkSessionIframeUri) {
        this.checkSessionIframeUri = checkSessionIframeUri;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getAuthorizationEndpointUri() {
        return authorizationEndpointUri;
    }

    public void setAuthorizationEndpointUri(String authorizationEndpointUri) {
        this.authorizationEndpointUri = authorizationEndpointUri;
    }

    public String[] getClaimsSupported() {
        return claimsSupported;
    }

    public void setClaimsSupported(String[] claimsSupported) {
        this.claimsSupported = claimsSupported;
    }

    public String getTokenEndpointUri() {
        return tokenEndpointUri;
    }

    public void setTokenEndpointUri(String tokenEndpointUri) {
        this.tokenEndpointUri = tokenEndpointUri;
    }

    public String[] getResponseTypesSupported() {
        return responseTypesSupported;
    }

    public void setResponseTypesSupported(String[] responseTypesSupported) {
        this.responseTypesSupported = responseTypesSupported;
    }

    public String getEndSssionEndpoint() {
        return endSssionEndpoint;
    }

    public void setEndSssionEndpoint(String endSssionEndpoint) {
        this.endSssionEndpoint = endSssionEndpoint;
    }

    public String getUserinfoEndpointUri() {
        return userinfoEndpointUri;
    }

    public void setUserinfoEndpointUri(String userinfoEndpointUri) {
        this.userinfoEndpointUri = userinfoEndpointUri;
    }

    public String getJwksUri() {
        return jwksUri;
    }

    public void setJwksUri(String jwksUri) {
        this.jwksUri = jwksUri;
    }

    public String[] getSubjectTypesSupported() {
        return subjectTypesSupported;
    }

    public void setSubjectTypesSupported(String[] subjectTypesSupported) {
        this.subjectTypesSupported = subjectTypesSupported;
    }

    public String[] getIdTokenSigningAlgSupported() {
        return idTokenSigningAlgSupported;
    }

    public void setIdTokenSigningAlgSupported(String[] idTokenSigningAlgSupported) {
        this.idTokenSigningAlgSupported = idTokenSigningAlgSupported;
    }

    public String getRegistrationEndpointUri() {
        return registrationEndpointUri;
    }

    public void setRegistrationEndpointUri(String registrationEndpointUri) {
        this.registrationEndpointUri = registrationEndpointUri;
    }
}
