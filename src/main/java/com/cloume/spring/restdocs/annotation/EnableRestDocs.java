package com.cloume.spring.restdocs.annotation;

import com.cloume.spring.restdocs.RestDocsConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * Created by Gang on 2017/08/03.
 */
@EnableAspectJAutoProxy
@Import(RestDocsConfiguration.class)
public @interface EnableRestDocs {
}
