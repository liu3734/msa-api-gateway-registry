package com.msa.api.gateway.registry;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * The interface Enable api gateway registry.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({ApplicationGatewayClientRegisterAutoConfiguration.class})
public @interface EnableApiGatewayRegistryClient {
}
