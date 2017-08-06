package com.cloume.spring.restdocs.processor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloume.spring.restdocs.RestDocBuilder;
import com.cloume.spring.restdocs.RestDocBuilder.RestDocMethodBuilder;
import com.cloume.spring.restdocs.annotation.EnableRestDocs;
import com.cloume.spring.restdocs.annotation.RestDoc;
import com.cloume.spring.restdocs.annotation.RestMethod;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Gang on 2017/08/03.
 */
public class RestDocumentProcessor implements BeanPostProcessor {
    @Autowired RestDocBuilder builder;
    
    void onMethodParamFound(RestDocMethodBuilder mb, Method method, Parameter param) {
    	String name = param.getName();
        boolean optional = true;
        if(param.isAnnotationPresent(RequestParam.class)) {
            RequestParam rp = param.getAnnotation(RequestParam.class);
            if(!rp.name().isEmpty()) name = rp.name();
            if(!rp.value().isEmpty()) name = rp.value();
            optional = !rp.required();
        } else if (param.isAnnotationPresent(RequestBody.class)) {
        	
        }
        mb.parameter(name).type(param.getType().getSimpleName()).optional(optional).end();
    }
    
    void OnMethodFound(Object controller, String[] baseUris, Method method) {
    	RequestMapping rm = method.getAnnotation(RequestMapping.class);
    	String[] uris = (rm.value() == null ? rm.path() : rm.value());
    	if(uris == null || uris.length == 0) uris = new String[] { "" };
    	uris = Arrays.stream(uris)
    		.map(p -> Arrays.stream(baseUris)
    				.map(bp -> bp + p)
    				.toArray(String[]::new))
    		.reduce((a, b) -> {
    			return ArrayUtils.addAll(a, b);
    		})
    		.orElse(new String[] { "" });
    	
    	RestDoc rd = method.getAnnotation(RestDoc.class);
    	RestMethod mtd = method.getAnnotation(RestMethod.class);
    	
    	String request = "", response = "";
    	if(mtd != null) {
    		try {
    			if(!mtd.requestExampleClass().equals(Void.class)) {
    				request = new ObjectMapper().writerWithDefaultPrettyPrinter()
    						.writeValueAsString(mtd.requestExampleClass().newInstance());
    			} else {
    				request = mtd.requestExampleText();
    			}
    			
    			if(!mtd.responseExampleClass().equals(Void.class)) {
    				response = new ObjectMapper().writerWithDefaultPrettyPrinter()
    						.writeValueAsString(mtd.responseExampleClass().newInstance());
    			} else {
    				response = mtd.responseExampleText();
    			}
			} catch (JsonProcessingException | InstantiationException | IllegalAccessException e) {
				System.err.printf("exp: %s \n", e.getMessage());
			}
    	}
    	
        RestDocBuilder.RestDocMethodBuilder mb = builder.method(method.getName())
        		.usage(rd != null ? rd.usage() : "{method usage}")
        		.request(request)
        		.response(response.isEmpty() ? "{response example}" : response)
        		.uris(uris)
                .methods(Arrays.stream(rm.method()).map(i -> i.name()).toArray(String[]::new));
        for (Parameter param : method.getParameters()) {
        	if(Arrays.asList(
        			"HttpServletRequest", 
        			"HttpServletResponse").contains(
        					param.getType().getSimpleName())) {
        		continue;
        	}
			onMethodParamFound(mb, method, param);
		}
        mb.end();
    }
    
    void onRestControllerFound(Object controller) {
    	String[] baseUris = new String[] { "" };
    	RequestMapping prm = controller.getClass().getAnnotation(RequestMapping.class);
    	if(prm != null) baseUris = (prm.value() == null ? prm.path() : prm.value());
    	
    	List<Method> methods = MethodUtils.getMethodsListWithAnnotation(
    			controller.getClass(), RequestMapping.class);
    	for (Method m : methods) {
    		OnMethodFound(controller, baseUris, m);
    	}
    }
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    	if (bean.getClass().isAnnotationPresent(EnableRestDocs.class)) {
    		EnableRestDocs erd = bean.getClass().getAnnotation(EnableRestDocs.class);
    		if(erd != null) {
    			builder.root().version(erd.version())
    				.usage(erd.usage())
    				.name(erd.name());
    		}
        } else if (bean.getClass().isAnnotationPresent(RestController.class)) {
        	onRestControllerFound(bean);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
