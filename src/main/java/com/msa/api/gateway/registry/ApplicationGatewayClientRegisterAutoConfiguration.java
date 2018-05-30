package com.msa.api.gateway.registry;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Application gateway client register auto configuration.
 */
@Configuration
@EnableConfigurationProperties(ApplicationGatewayProperties.class)
public class ApplicationGatewayClientRegisterAutoConfiguration {
    /**
     * Application gateway client register application gateway client register.
     *
     * @param properties the properties
     * @return the application gateway client register
     */
    @Bean
    @ConditionalOnMissingBean
    public ApplicationGatewayClientRegister applicationGatewayClientRegister(ApplicationGatewayProperties properties) {
        return new ApplicationGatewayClientRegister(properties);
    }
}
