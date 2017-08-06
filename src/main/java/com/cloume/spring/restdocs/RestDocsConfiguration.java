package com.cloume.spring.restdocs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloume.spring.restdocs.processor.RestDocumentProcessor;

/**
 * Created by Gang on 2017/08/03.
 */
@Configuration
public class RestDocsConfiguration {
    @Bean
    public RestDocumentProcessor restControllerProcessor() {
        return new RestDocumentProcessor();
    }

    @Bean
    public RestDocBuilder restDocBuilder() { 
    	return new RestDocBuilder();
    }

    @Controller
    @RequestMapping(value = "${restdocs.base:/docs.md}")
    static class Docs {
        @Autowired RestDocBuilder builder;

        @RequestMapping(method = RequestMethod.GET, produces = {MediaType.TEXT_MARKDOWN_VALUE})
        @ResponseBody public String md() {
            //builder.reset();
            String document = builder.generate();

            return document;
        }
        
        @RequestMapping(value = "/html", method = RequestMethod.GET, produces = {MediaType.TEXT_HTML_VALUE})
        @ResponseBody public String html() {
            //builder.reset();
            String document = builder.strapdown();

            return document;
        }
    }
}
