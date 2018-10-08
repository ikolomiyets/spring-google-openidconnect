package com.github.fromi.openidconnect.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.context.NullSecurityContextRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final String LOGIN_URL = "/login";

    @Value("${oauth2.logoutUri}")
    private String logoutUri;

    @Autowired
    private OAuth2ClientContextFilter oAuth2ClientContextFilter;

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint(LOGIN_URL);
    }

    @Bean
    public OpenIDConnectAuthenticationFilter openIdConnectAuthenticationFilter() {
        return new OpenIDConnectAuthenticationFilter(LOGIN_URL);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .addFilterAfter(oAuth2ClientContextFilter, AbstractPreAuthenticatedProcessingFilter.class)
            .addFilterAfter(openIdConnectAuthenticationFilter(), OAuth2ClientContextFilter.class)
            .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
            .and()
                .authorizeRequests()
                .antMatchers(GET, "/favicon.ico", LOGIN_URL).permitAll()
                .antMatchers(GET, "/test").hasRole("OPERATOR")
                .antMatchers(GET, "/admin").hasRole("ADMINISTRATOR")
                .antMatchers(GET, "/**").access("hasRole('ROLE_OPERATOR') or hasRole('ROLE_ADMINISTRATOR')")
            .and()
                .logout()
                .logoutSuccessUrl("/")
                .logoutUrl(logoutUri);
    }
}
