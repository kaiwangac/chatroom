package com.chatroom.config;

import com.chatroom.constant.RouterPath;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Created by wangkai on 2017/10/29.
 */
@SpringBootConfiguration
public class VertxConfig {

    @Bean
    public Vertx vertx() {
        Vertx vertx = Vertx.vertx();
        return vertx;
    }

    @Bean
    public Router mainRouter(Vertx vertx) {
        Router router = Router.router(vertx);
        router.route(RouterPath.STATIC).handler(StaticHandler.create());
        return router;
    }

    @Bean
    @Primary
    public Router restRouter(Vertx vertx, Router mainRouter) {
        Router router = Router.router(vertx);
        mainRouter.mountSubRouter(RouterPath.REST, router);
        return router;
    }
}
