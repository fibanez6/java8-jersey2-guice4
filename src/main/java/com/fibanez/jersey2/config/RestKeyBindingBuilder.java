package com.fibanez.jersey2.config;

public interface RestKeyBindingBuilder {

    void build();

    RestKeyBindingBuilder packages(String ... packages);

    RestKeyBindingBuilder classnames(String ... classnames);
}
