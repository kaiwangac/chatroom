package com.chatroom.config;

import com.chatroom.verticle.HttpServerVerticle;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class HttpServerDeploy implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerDeploy.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        Vertx vertx = context.getBean(Vertx.class);
        HttpServerVerticle httpServerVerticle = context.getBean(HttpServerVerticle.class);
        vertx.deployVerticle(httpServerVerticle);
    }
}
