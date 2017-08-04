package com.cloume.spring.restdocs.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Gang on 2017/08/04.
 */
public class DocMethod {
    DocResponse response;
    String name;
    String[] methods = new String[]{};
    String description;
    Collection<DocParam<?>> params;


    Collection<DocParam<?>> getParams() {
        return params == null ? params = new ArrayList<>() : params;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMethods(String[] methods) {
        this.methods = methods;
    }

    public void addParam(DocParam<?> newParam) {
        if(newParam == null) return;
        getParams().add(newParam);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("### ** %s ** %s ###\n\n",
                Arrays.stream(methods)
                        .map(s -> String.format("``%s``", s))
                        .reduce((a, b) -> String.format("%s,%s", a, b)).orElse("ALL"),
                name));
        sb.append(String.format("%s\n\n", description));
        sb.append("|name|type|optional|description|\n");
        sb.append("|---|---|:---:|---|\n");
        getParams().stream().forEach(p -> {
            sb.append(String.format("| ** %s ** |%s|%s|%s|\n", p.name, p.type, p.optional, p.description));
        });

        return sb.toString();
    }
}
