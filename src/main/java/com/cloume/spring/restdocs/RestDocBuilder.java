package com.cloume.spring.restdocs;

import com.cloume.spring.restdocs.RestDocBuilder.RestDocMethodBuilder;
import com.cloume.spring.restdocs.data.DocMethod;
import com.cloume.spring.restdocs.data.DocParam;
import com.cloume.spring.restdocs.data.DocRoot;

/**
 * Created by Gang on 2017/08/04.
 */
public class RestDocBuilder {

    static DocRoot root;
    DocRoot doc() { return root == null ? root = new DocRoot() : root; }

    public void reset() {
        doc().clear();
    }

    public class RestDocMethodBuilder{
        DocMethod newMethod = null;
        DocMethod creating() { return newMethod == null ? newMethod = new DocMethod() : newMethod; }

        RestDocMethodBuilder name(String methodName) {
            creating().setName(methodName);
            return this;
        }

        public RestDocMethodBuilder methods(String[] methods) {
            creating().setMethods(methods);
            return this;
        }
        
        public RestDocMethodBuilder uris(String[] uris) {
        	creating().setUris(uris);
        	return this;
        }
        
        public RestDocMethodBuilder usage(String usage) {
        	creating().setUsage(usage);
        	return this;
		}
        
        public RestDocMethodBuilder response(String response) {
        	creating().setResponse(response);
			return this;
		}

        RestDocBuilder and() {
            end();
            return RestDocBuilder.this;
        }

        public void end() {
            if(newMethod != null) {
                doc().addMethod(newMethod);
                newMethod = null;
            }
        }

        ///parameters

        RestDocMethodParameterBuilder restDocMethodParameterBuilder;

        {
            restDocMethodParameterBuilder = new RestDocMethodParameterBuilder();
        }

        public RestDocMethodParameterBuilder parameter() {
            return restDocMethodParameterBuilder;
        }

        public RestDocMethodParameterBuilder parameter(String parameterName) {
            return parameter().name(parameterName);
        }

        public class RestDocMethodParameterBuilder {
            DocParam<?> newParam = null;
            DocParam<?> creating() { return newParam == null ? newParam = new DocParam<>() : newParam; }

            public RestDocMethodParameterBuilder name(String paramName) {
                creating().setName(paramName);
                return this;
            }

            public RestDocMethodParameterBuilder type(String paramType) {
                creating().setType(paramType);
                return this;
            }

            public RestDocMethodParameterBuilder description(String paramDescription) {
                creating().setDescription(paramDescription);
                return this;
            }

            public RestDocMethodParameterBuilder optional(boolean op) {
                creating().setOptional(op);
                return this;
            }

            public RestDocMethodBuilder and() {
                if(newParam != null) {
                    RestDocMethodBuilder.this.creating().addParam(newParam);
                    newParam = null;
                }
                return RestDocMethodBuilder.this;
            }

            public RestDocMethodBuilder end() {
                return and();
            }
        }
    }
    RestDocMethodBuilder restDocMethodBuilder;

    {
        restDocMethodBuilder = new RestDocMethodBuilder();
    }

    public RestDocMethodBuilder method() {
        return restDocMethodBuilder;
    }
    public RestDocMethodBuilder method(String methodName) {
        return method().name(methodName);
    }

    public String generate() {
        return doc().toString();
    }
}
