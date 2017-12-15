package com.chatroom.handler;

import com.chatroom.handler.impl.JerseyHandlerImpl;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import org.glassfish.jersey.server.spi.Container;

import java.net.URI;

public interface JerseyHandler extends Handler<RoutingContext> {
    static JerseyHandler create(URI baseUri, Vertx vertx, Container container) {
        return new JerseyHandlerImpl(baseUri, vertx, container);
    }
}
