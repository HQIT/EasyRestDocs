package com.cloume.spring.restdocs.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Gang on 2017/08/04.
 */
public class DocMethod {
    String name;
    String[] methods = new String[] {};
    String[] uris = new String[] {};
    String[] scopes = new String[] {};
    Map<Integer, String> errors = new HashMap<>();
    String usage;
    String request, response;
    
    Collection<DocParam> params;
	
    Collection<DocParam> getParams() {
        return params == null ? params = new ArrayList<>() : params;
    }
    
    Map<Integer, String> getErrors() {
    	return errors;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setUsage(String usage) {
		this.usage = usage;
	}

    public void setMethods(String[] methods) {
        this.methods = methods;
    }
    
    public void setUris(String[] uris) {
    	this.uris = uris;
    }
    
    public void setScopes(String[] scopes) {
    	this.scopes = scopes;
    }
    
    public void setResponse(String response) {
    	this.response = response;
    }
    
    public void setRequest(String request) {
		this.request = request;
	}

    public void addParam(DocParam newParam) {
        if(newParam == null) return;
        getParams().add(newParam);
    }
    
    public void addError(int code, String message) {
    	getErrors().put(code, message);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("### %s %s ###\n\n",
                Arrays.stream(methods)
                        .map(s -> String.format("``%s``", s))
                        .reduce((a, b) -> String.format("%s,%s", a, b)).orElse("``ALL``"),
                name));
        
        ///scopes
        sb.append(String.format("**scopes:** %s\n\n", 
        		Arrays.stream(scopes)
        			.map(s -> String.format("``%s``", s))
        			.reduce((a, b) -> String.format("%s, %s", a, b)).orElse("``NONE``")
        		));
        
        ///usage
        sb.append(String.format("**usage:** %s\n\n", usage.isEmpty() ? "{method usage}" : usage));
        
        ///uris
        sb.append(String.format("**uris:** %s\n\n", 
        		Arrays.stream(uris)
        			.map(s -> String.format("``%s``", s))
        			.reduce((a, b) -> String.format("%s, %s", a, b)).orElse("``NONE``")
        		));
        
        if(!getParams().isEmpty()) {
	        ///parameters
	        sb.append("#### parameters: ####\n|name|type|optional|description|\n");
	        sb.append("|---|---|:---:|---|\n");
	        getParams().stream().forEach(p -> {
	            sb.append(String.format("| **%s** |%s|%s|%s|\n", p.name, p.type, p.optional, p.description));
	        });
        }
        
        ///request example
        if(request != null && !request.isEmpty()) {
        	sb.append(String.format("#### request: ####\n```\n%s\n```\n\n", request));
        }
        
        ///response example:
        sb.append(String.format("#### response: ####\n```\n%s\n```\n\n",
        		StringUtils.isBlank(response) ? "{response example}" : response
        		));
        
        /// error codes
        sb.append(String.format("#### errors: ####\n%s\n\n",
        		getErrors().entrySet().stream()
        			.map(e -> String.format("* ``%d`` : %s\n", e.getKey(), e.getValue()))
        			.reduce((a, b) -> a + b)
        			.orElse("*NOT SET*")
        		));
        
        sb.append("\n\n------\n\n\n");

        return sb.toString();
    }
}
