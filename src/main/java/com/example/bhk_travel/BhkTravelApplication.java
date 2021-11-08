package com.example.bhk_travel;

import java.net.URL;
import java.net.URLEncoder;

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

	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(BhkTravelApplication.class);
	}

}
