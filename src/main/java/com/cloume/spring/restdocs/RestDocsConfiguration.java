package com.cloume.spring.restdocs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloume.spring.restdocs.processor.RestControllerProcessor;

/**
 * Created by Gang on 2017/08/03.
 */
@Configuration
public class RestDocsConfiguration {
    @Bean
    public RestControllerProcessor restControllerProcessor() {
        return new RestControllerProcessor();
    }

    @Bean
    public RestDocBuilder restDocBuilder() { 
    	return new RestDocBuilder();
    }

    @Controller
    @RequestMapping(value = "${restdocs.base:/docs.md}", produces = {MediaType.TEXT_MARKDOWN_VALUE})
    static class Docs {
        @Autowired RestDocBuilder builder;

        @RequestMapping(method = RequestMethod.GET)
        @ResponseBody public Object docs() {
            //builder.reset();
            String document = builder
            /*        .method("hello")
                        .parameter("who").optional(true)
                    .and()
                        .parameter("go").description("where to go?").type("string")
                    .and()
                    .and()
                        .method()
                    .name("hello2")
                    .and()*/
                    .generate();

            return document;
        }
    }
}
