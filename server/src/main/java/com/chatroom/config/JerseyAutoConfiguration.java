package com.chatroom.config;

import com.chatroom.jersey.HttpContainer;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.core.Application;

@Configuration
public class JerseyAutoConfiguration {

    @Bean
    public ResourceConfig resourceConfig() {
        return new ResourceConfig().packages("com.chatroom.rest");
    }

    @Bean
    public Container container(Application application) {
        return new HttpContainer(application);
    }
}
