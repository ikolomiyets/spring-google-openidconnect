package com.github.fromi.openidconnect.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${oauth2.wso2Client}")
    private String clientId;

    @Value("${oauth2.wso2Secret}")
    private String clientSecret;

    @Value("${oauth2.checkTokenUri:}")
    private String checkTokenUri;

    @Value("${oauth2.userInfoUri:}")
    private String userInfoUri;

    @Autowired
    private OpenIdConfiguration config;

    public ResourceServerConfiguration() {
        if (config != null) {
            userInfoUri = config.getUserinfoEndpointUri();
        }
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(clientId);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers().antMatchers("/rest/**").and().authorizeRequests()
                .antMatchers("/rest/admin").hasRole("ADMINISTRATOR")
                .antMatchers("/rest/**").hasRole("OPERATOR");
    }


    @Bean
    public RemoteTokenServices getRemoteTokenServices () {
        WSO2TokenServices rts = new WSO2TokenServices();
        rts.setCheckTokenEndpointUrl(checkTokenUri);
        rts.setClientId(this.clientId);
        rts.setClientSecret(this.clientSecret);
        rts.setAccessTokenConverter(getAccessTokenConverter());
        return rts;
    }

    @Bean
    public AccessTokenConverter getAccessTokenConverter() {
        WSO2AccessTokenConverter converter = new WSO2AccessTokenConverter();
        converter.setUserInfoUri(this.userInfoUri);
        return converter;
    }

}
