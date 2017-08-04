package com.cloume.spring.restdocs.processor;

import com.cloume.spring.restdocs.RestDocBuilder;
import org.apache.commons.lang3.AnnotationUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;

/**
 * Created by Gang on 2017/08/03.
 */
public class RestControllerProcessor implements BeanPostProcessor {
    @Autowired RestDocBuilder builder;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RestController.class)) {
                List<Method> methods = org.apache.commons.lang3.reflect.MethodUtils.getMethodsListWithAnnotation(bean.getClass(), RequestMapping.class);
                for (Method m : methods) {
                    RequestMapping rm = m.getAnnotation(RequestMapping.class);
                    RestDocBuilder.RestDocMethodBuilder mb = builder.method(Arrays.stream(rm.value() == null ? rm.path() : rm.value())
                            .reduce((a, b) -> a + "," + b).get())
                            .methods(Arrays.stream(rm.method()).map(i -> i.name()).toArray(String[]::new));
                    Arrays.stream(m.getParameters()).forEach(p -> {
                        String name = p.getName();
                        boolean optional = true;
                        if(p.isAnnotationPresent(RequestParam.class)) {
                            RequestParam rp = p.getAnnotation(RequestParam.class);
                            if(!rp.name().isEmpty()) name = rp.name();
                            if(!rp.value().isEmpty()) name = rp.value();
                            optional = !rp.required();
                        }
                        mb.parameter(name).type(p.getType().getSimpleName()).optional(optional).end();
                    });
                    mb.end();
                }
            }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        return o;
    }
}
