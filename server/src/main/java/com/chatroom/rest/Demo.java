package com.chatroom.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("test")
public class Demo {
    @GET
    public String get() {
        return "test";
    }

    @GET
    @Path(value="/liu")
    public String get2() {return "liu";}
}
