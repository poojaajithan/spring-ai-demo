package com.dev.ai.labs.spring_ai_demo.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.ai.vectorstore")
public class VectorStoreProperties {

	private String vectorStorePath;
	private List<Resource> documentsToLoad;

}