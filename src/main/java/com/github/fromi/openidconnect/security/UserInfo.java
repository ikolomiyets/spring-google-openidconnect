package com.github.fromi.openidconnect.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

public class UserInfo {
    private final String name;
    private final String givenName;
    private final String familyName;
    private final String email;
    private final List<GrantedAuthority> authorities;

    @JsonCreator
    public UserInfo(@JsonProperty("sub") String name,
                    @JsonProperty("given_name") String givenName,
                    @JsonProperty("family_name") String familyName,
                    @JsonProperty("email") String email,
                    @JsonProperty("groups") String groups) {
        this.name = name;
        this.givenName = givenName;
        this.familyName = familyName;
        this.email = email;
        this.authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(groups);
    }

    public String getName() {
        return name;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getEmail() {
        return email;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
