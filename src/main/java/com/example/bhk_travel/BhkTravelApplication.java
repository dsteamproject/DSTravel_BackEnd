package com.example.bhk_travel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan(basePackages = { "com.example.listener" })
@PropertySource("classpath:global.properties")
@ComponentScan(basePackages = { "com.example.controller", "com.example.security", "com.example.jwt",
		"com.example.service" })
@EntityScan(basePackages = { "com.example.entity" })
@EnableJpaRepositories(basePackages = { "com.example.repository" })
@EnableScheduling
public class BhkTravelApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BhkTravelApplication.class, args);
		System.out.println("server start");

		// List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		// Map<String, Object> a = new HashMap<>();
		// // a.put("a", "a");
		// if (a.size() != 0)
		// list1.add(a);
		// System.out.println(list1);

	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(BhkTravelApplication.class);
	}

}
