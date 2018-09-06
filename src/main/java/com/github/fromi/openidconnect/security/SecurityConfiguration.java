package com.github.fromi.openidconnect.security;

import static org.springframework.http.HttpMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final String LOGIN_URL = "/login";

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
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and().authorizeRequests()
                .antMatchers(GET, "/", LOGIN_URL, "/rest/**").permitAll()
                .antMatchers(GET, "/test", "/admin").hasRole("OPERATOR")
                .antMatchers(GET, "/admin").hasRole("ADMINISTRATOR");
    }
}
