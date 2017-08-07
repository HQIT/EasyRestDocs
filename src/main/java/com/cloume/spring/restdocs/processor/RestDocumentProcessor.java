package com.cloume.spring.restdocs.processor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
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
import com.cloume.spring.restdocs.annotation.RestParam;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Gang on 2017/08/03.
 */
public class RestDocumentProcessor implements BeanPostProcessor {
    @Autowired RestDocBuilder builder;
    
    void onMethodParamFound(RestDocMethodBuilder mb, Method method, Parameter param) {
    	String name = param.getName(), type = "", description = "";
        boolean optional = true;
        if(param.isAnnotationPresent(RequestParam.class)) {
            RequestParam rp = param.getAnnotation(RequestParam.class);
            if(!rp.name().isEmpty()) name = rp.name();
            if(!rp.value().isEmpty()) name = rp.value();
            optional = !rp.required();
        
        } else if (param.isAnnotationPresent(RequestBody.class)) {
        	type = "Object";
        	return;
        }
        
        if(param.isAnnotationPresent(RestParam.class)) {
        	RestParam rp = param.getAnnotation(RestParam.class);
        	description = rp.value().isEmpty() ? rp.description() : rp.value();
        	if(name.startsWith("arg") || name.isEmpty()) name = rp.name();
        }
        
        mb.parameter(name)
        	.type(type.isEmpty() ? param.getType().getSimpleName() : type)
        	.optional(optional)
        	.description(description.isEmpty() ? "--" : description)
        	.end();
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
    	
    	String[] scopes = new String[] {};
    	String name = "", request = "", response = "", usage = "";
    	if(rd != null) usage = rd.usage();
    	
    	if(mtd != null) {
    		name = mtd.name();
    		scopes = mtd.scopes();
    		usage += mtd.usage();
    		
    		try {
    			if(!mtd.requestExampleClass().equals(Void.class)) {
    				request = new ObjectMapper().writerWithDefaultPrettyPrinter()
    						.writeValueAsString(mtd.requestExampleClass().newInstance());
    			} else {
    				request = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
    						new ObjectMapper().readTree(mtd.requestExampleText()));
    			}
    		} catch (InstantiationException | IllegalAccessException | IOException e) {
				System.err.printf("exp: %s \n", e.getMessage());
			}
    		
    		try {
    			if(!mtd.responseExampleClass().equals(Void.class)) {
    				response = new ObjectMapper().writerWithDefaultPrettyPrinter()
    						.writeValueAsString(mtd.responseExampleClass().newInstance());
    			} else {
    				response = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
    						new ObjectMapper().readTree(mtd.responseExampleText()));
    			}
			} catch (InstantiationException | IllegalAccessException | IOException e) {
				System.err.printf("exp: %s \n", e.getMessage());
			}
    	}
    	
        RestDocBuilder.RestDocMethodBuilder mb = builder.method(name.isEmpty() ? method.getName() : name)
        		.usage(usage.isEmpty() ? "{method usage}" : usage)
        		.scopes(scopes)
        		.request(request)
        		.response(response.isEmpty() ? "{response example}" : response)
        		.uris(uris)
                .methods(Arrays.stream(rm.method()).map(i -> i.name()).toArray(String[]::new));
        
        /// errors
        if(mtd != null) {
        	Arrays.stream(mtd.errors()).forEach(e -> mb.error(e.code(), e.message()));
        }
        
        /// parameters
        for (Parameter param : method.getParameters()) {
        	if(Arrays.asList(
        			"HttpServletRequest", 
        			"HttpServletResponse",
        			"Principal").contains(
        					param.getType().getSimpleName())) {
        		continue;
        	}
			onMethodParamFound(mb, method, param);
		}
        mb.end();
    }
    
    void onRestControllerFound(Object controller) {
    	String[] baseUris = null;
    	RequestMapping prm = controller.getClass().getAnnotation(RequestMapping.class);
    	if(prm != null) baseUris = (prm.value() == null ? prm.path() : prm.value());
    	if(baseUris == null || baseUris.length == 0) baseUris = new String[] { "" };
    	
    	Method[] all = controller.getClass().getDeclaredMethods();
    	for(Method m: all) {
    		if(m.isAnnotationPresent(RequestMapping.class)) {
    			OnMethodFound(controller, baseUris, m);
    		}
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
