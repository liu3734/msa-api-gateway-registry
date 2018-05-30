package com.msa.api.gateway.registry;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The type Application gateway client register.
 */
@Getter
@Slf4j
public class ApplicationGatewayClientRegister implements EnvironmentAware, InitializingBean,
        ApplicationListener<EmbeddedServletContainerInitializedEvent> {
    /**
     * The constant ROOT_NODE.
     */
    private static final String ROOT_NODE = "/registry";

    /**
     * The Env.
     */
    private Environment env;

    /**
     * The Properties.
     */
    private ApplicationGatewayProperties properties;

    /**
     * The Zk client.
     */
    private ZkClient zkClient;

    /**
     * Instantiates a new Application gateway client register.
     *
     * @param properties the properties
     */
    public ApplicationGatewayClientRegister(ApplicationGatewayProperties properties) {
        this.properties = properties;
    }

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        String applicationName = env.getProperty("spring.application.name");
        Assert.hasText(applicationName, "please set spring.application.name in your properties file.");
        log.info(">>>>>>>===the application name is {}", applicationName);
        int port = event.getApplicationContext().getEmbeddedServletContainer().getPort();
        if (port == 0) {
            throw new IllegalArgumentException("get port from embedded ServletContainer error");
        }
        registryApplication(applicationName, port);
    }

    /**
     * Set the {@code Environment} that this object runs in.
     *
     * @param environment the environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    /**
     * Registry application.
     *
     * @param name the name
     * @param port the port
     */
    private void registryApplication(String name, int port) {
        if (!zkClient.exists(ROOT_NODE)) {
            zkClient.createPersistent(ROOT_NODE);
        }
        String appNode = ROOT_NODE + "/" + name;
        if (!zkClient.exists(appNode)) {
            zkClient.createPersistent(appNode);
        }
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            String hostAndPort = ":=" + host + ":" + port;
            zkClient.createEphemeralSequential(appNode + "/address-", hostAndPort);
        } catch (UnknownHostException e) {
            throw new IllegalStateException("get host address error");
        }
    }

    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     *
     * @throws Exception in the event of misconfiguration (such                   as failure to set an essential property) or if initialization fails.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        zkClient = new ZkClient(properties.getRegistryAddress(), 5000, 1000);
        log.debug(">>>>>>>>>===connect to zookeeper");
    }
}
