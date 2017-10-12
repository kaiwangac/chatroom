package com.chatroom.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.web.Router;
import org.h2.tools.Server;

import java.sql.SQLException;

/**
 * Created by wangkai on 2017/10/11.
 */
public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        initDataSource();

        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        router.route().handler(routingContext -> {

            // 所有的请求都会调用这个处理器处理
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain");

            // 写入响应并结束处理
            response.end("Hello World from Vert.x-Web!");
        });

        server.requestHandler(router::accept).listen(8080);
    }

    private void initDataSource() {
        try {
            Server.createTcpServer().start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        vertx.executeBlocking(future -> {
            try {
                Server server = Server.createTcpServer().start();
                future.complete(server);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, res -> {
            System.out.println("The result is: " + res.result());
            JsonObject config = new JsonObject()
                    .put("url", "jdbc:h2:file:~/.h2/db;AUTO_SERVER=TRUE")
                    .put("driver_class", "org.h2.Driver")
                    .put("user", "root")
                    .put("password", "123456");

            JDBCClient.createShared(vertx, config);
        });

//        JsonObject config = new JsonObject()
//                .put("url", "jdbc:h2:file:~/.h2/db;AUTO_SERVER=TRUE")
//                .put("driver_class", "org.h2.Driver")
//                .put("user", "root")
//                .put("password", "123456");
//
//        JDBCClient.createShared(vertx, config);
    }
}
