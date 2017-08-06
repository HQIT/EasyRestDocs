package com.cloume.spring.restdocs.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * Created by Gang on 2017/08/04.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestParam {
	@AliasFor("description")
	String value() default "";
	
	@AliasFor("value")
	String description() default "";
	
	String name() default "";
}
