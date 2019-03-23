package ru.bookshop.configuration;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class RestConfig extends ResourceConfig {

    public RestConfig() {
        packages("ru.bookshop.api.rest");
    }
}
