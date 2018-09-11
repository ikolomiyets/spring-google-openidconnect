package com.github.fromi.openidconnect.services;

import com.github.fromi.openidconnect.security.UserInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TestService {
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    public UserInfo getTest() {
        return (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public UserInfo getAdmin() {
        return (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
