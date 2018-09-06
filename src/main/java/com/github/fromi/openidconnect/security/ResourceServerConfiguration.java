package com.github.fromi.openidconnect.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${oauth2.wso2Client}")
    private String clientId;

    @Value("${oauth2.wso2Secret}")
    private String clientSecret;

    @Value("${oauth2.checkTokenUri}")
    private String checkTokenUri;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(clientId);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers()
                .antMatchers("/rest/**").and().authorizeRequests()
                .anyRequest().access("#oauth2.hasScope('read')");
    }


    @Bean
    RemoteTokenServices getRemoteTokenServices () {
        RemoteTokenServices rts = new RemoteTokenServices();
        rts.setCheckTokenEndpointUrl(checkTokenUri);
        rts.setClientId(this.clientId);
        rts.setClientSecret(this.clientSecret);
        return rts;
    }
}
