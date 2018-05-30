package com.msa.api.gateway.registry;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * The type Application gateway properties.
 */
@Data
@ConfigurationProperties(prefix = "spring.api.gateway")
@EnableConfigurationProperties(ApplicationGatewayProperties.class)
public class ApplicationGatewayProperties {

    /**
     * The Registry address.
     */
    private String registryAddress = "localhost:2181";
}
