package com.cloume.spring.restdocs.data;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Gang on 2017/08/04.
 */
public class DocRoot {
    String title;
    String version;
    String description;

    Collection<DocMethod> methods;

    Collection<DocMethod> getMethods() {
        return methods == null ? methods = new ArrayList<>() : methods;
    }

    public void addMethod(DocMethod method) {
        if(method == null) return;
        getMethods().add(method);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("# %s #\n\n", title == null ? "{Untitled}" : title));
        sb.append(String.format("`` %s ``\n\n", version == null ? "{Unversioned}" : version));
        sb.append(String.format("%s \n\n", description));

        sb.append(String.format("## METHODS: ## \n\n"));
        getMethods().stream().forEach(m -> sb.append(m.toString()));

        return sb.toString();
    }

    public void clear() {
        getMethods().clear();
    }

	public void setName(String name) {
		this.title = name;
	}
	public String getName() {
		return this.title;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public void setUsage(String description) {
		this.description = description;
	}
}
