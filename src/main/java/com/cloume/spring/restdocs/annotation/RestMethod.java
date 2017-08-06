package com.cloume.spring.restdocs.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Gang on 2017/08/04.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RestMethod {
    String responseExampleText() default "";
    
    String requestExampleText() default "";

	String name() default "";
	
	String usage() default "";

	Class<?> responseExampleClass() default Void.class;

	Class<?> requestExampleClass() default Void.class;

	String[] scopes() default {};
}
