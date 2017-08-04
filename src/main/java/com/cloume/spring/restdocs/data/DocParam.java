package com.cloume.spring.restdocs.data;

/**
 * Created by Gang on 2017/08/04.
 */
public class DocParam<T> {
    T payload;

    String name;
    boolean optional;
    String type;
    String description;

    public void setName(String name) {
        this.name = name;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
