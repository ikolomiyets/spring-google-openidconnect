package com.github.fromi.openidconnect.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.fromi.openidconnect.security.role.RoleExtender;
import com.github.fromi.openidconnect.security.role.RoleExtenderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
                    @JsonProperty("groups") String[] groups) {
        RoleExtender roleExtender = RoleExtenderFactory.getRoleExtender();

        this.name = name;
        this.givenName = givenName;
        this.familyName = familyName;
        this.email = email;
        final StringBuilder sb = new StringBuilder();
        Arrays.asList(groups).forEach(group -> sb.append(group).append(","));
        sb.deleteCharAt(sb.length() - 1);
        this.authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(roleExtender != null ? roleExtender.extendRoles(sb.toString()) : sb.toString());
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
