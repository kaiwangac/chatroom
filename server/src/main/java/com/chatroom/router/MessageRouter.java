package com.chatroom.router;

import com.chatroom.constant.RouterPath;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageRouter implements InitializingBean {

    @Autowired
    private Router router;

    @Override
    public void afterPropertiesSet() throws Exception {
        router.get(RouterPath.MESSAGE).handler(this::getMessage);
    }

    public void getMessage(RoutingContext routingContext) {
//        String contentType = routingContext.request().getHeader(HttpHeaders.CONTENT_TYPE);
        String roomNumber = routingContext.request().getParam("roomNumber");
        String messageId = routingContext.request().getParam("messageId");    }
}
