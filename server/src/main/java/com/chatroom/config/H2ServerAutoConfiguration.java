package com.chatroom.config;

import org.h2.tools.Server;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
@ConditionalOnProperty(prefix = "h2.server", name = "enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(H2ServerProperties.class)
public class H2ServerAutoConfiguration {
    private final H2ServerProperties properties;

    public H2ServerAutoConfiguration(H2ServerProperties properties) {
        this.properties = properties;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnProperty(prefix = "h2.server.tcp", name = "enabled", havingValue = "true", matchIfMissing = false)
    public Server h2TcpServer() throws SQLException {
        return Server.createTcpServer(this.properties.getTcp().getArgs());
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnProperty(prefix = "h2.server.web", name = "enabled", havingValue = "true", matchIfMissing = false)
    public Server h2WebServer() throws SQLException {
        return Server.createWebServer(this.properties.getWeb().getArgs());
    }

}
