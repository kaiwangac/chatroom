package com.chatroom.config;

import com.chatroom.handler.JerseyHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.glassfish.jersey.server.spi.Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Created by wangkai on 2017/10/29.
 */
@Configuration
@AutoConfigureAfter(JerseyAutoConfiguration.class)
public class VertxAutoConfiguration {

    @Autowired
    private Container container;

    @Bean
    public Vertx vertx() {
        Vertx vertx = Vertx.vertx();
        return vertx;
    }

    @Bean
    public Router mainRouter(Vertx vertx) {
        Router router = Router.router(vertx);
        router.route("/static/*").handler(StaticHandler.create());
        return router;
    }

    @Bean
    @Primary
    public Router restRouter(Vertx vertx, Router mainRouter) {
        Router router = Router.router(vertx);
        mainRouter.mountSubRouter("/rest/v1/", router);

        URI baseUri = UriBuilder.fromUri("/rest/v1/").build();
        router.route().handler(JerseyHandler.create(baseUri, vertx, container));
        return router;
    }
}
