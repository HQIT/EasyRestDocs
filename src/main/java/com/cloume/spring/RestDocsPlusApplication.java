package com.cloume.spring;

import com.cloume.spring.restdocs.annotation.EnableRestDocs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRestDocs
public class RestDocsPlusApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestDocsPlusApplication.class, args);
	}
}
