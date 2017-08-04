package com.cloume.spring.restdocs.annotation;

/**
 * Created by Gang on 2017/08/04.
 */
public @interface RestDoc {
    String usage() default "";
    String[] scopes() default {};
}
