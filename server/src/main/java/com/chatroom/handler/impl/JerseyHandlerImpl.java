package com.chatroom.handler.impl;

import com.chatroom.handler.JerseyHandler;
import com.chatroom.jersey.ResponseWriter;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import org.glassfish.jersey.internal.PropertiesDelegate;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.spi.Container;

import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JerseyHandlerImpl implements JerseyHandler {
    private URI baseUri;
    private Vertx vertx;
    private Container container;

    public JerseyHandlerImpl(URI baseUri, Vertx vertx, Container container) {
        this.baseUri = baseUri;
        this.vertx = vertx;
        this.container = container;
    }

    @Override
    public void handle(RoutingContext event) {
        HttpServerRequest request = event.request();
        ContainerRequest requestContext = new ContainerRequest(
                baseUri, UriBuilder.fromUri(request.uri()).build(), request.method().name(), getSecurityContext(),
                getPropertiesDelegate());
        requestContext.setWriter(new ResponseWriter(request, vertx));
        container.getApplicationHandler().handle(requestContext);
    }

    private SecurityContext getSecurityContext() {
        return new SecurityContext() {

            @Override
            public boolean isUserInRole(final String role) {
                return false;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public Principal getUserPrincipal() {
                return null;
            }

            @Override
            public String getAuthenticationScheme() {
                return null;
            }

        };
    }

    private PropertiesDelegate getPropertiesDelegate() {
        return new PropertiesDelegate() {

            private final Map<String, Object> properties = new HashMap<>();

            @Override
            public Object getProperty(String name) {
                return properties.get(name);
            }

            @Override
            public Collection<String> getPropertyNames() {
                return properties.keySet();
            }

            @Override
            public void setProperty(String name, Object object) {
                properties.put(name, object);
            }

            @Override
            public void removeProperty(String name) {
                properties.remove(name);
            }
        };
    }
}
