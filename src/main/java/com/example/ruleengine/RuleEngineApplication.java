package com.example.ruleengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class RuleEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(RuleEngineApplication.class, args);
	}

}
