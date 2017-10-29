package com.chatroom.config;

import com.chatroom.verticle.MainVerticle;
import io.vertx.core.Vertx;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Created by wangkai on 2017/10/29.
 */
@SpringBootConfiguration
public class VertxConfig {

    @Bean
    public Vertx vertx() {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(MainVerticle.class.getName());
        return vertx;
    }
}
