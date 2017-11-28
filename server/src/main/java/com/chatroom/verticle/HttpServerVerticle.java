package com.chatroom.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by wangkai on 2017/10/11.
 */
@Component
public class HttpServerVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerVerticle.class);

    @Autowired
    @Qualifier("mainRouter")
    private Router router;

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        HttpServer server = vertx.createHttpServer();

        server.requestHandler(router::accept).listen(8080, res -> {
            if (res.succeeded()) {
                LOGGER.info("Http Server started on port(s):{}(http)", 8080);
            } else {
                LOGGER.error("Http Server Startup failed");
            }
        });
    }

}
