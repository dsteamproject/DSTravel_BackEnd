package com.example.bhk_travel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@PropertySource("classpath:global.properties")
@ComponentScan(basePackages = { "com.example.controller", "com.example.security", "com.example.jwt",
		"com.example.service" })
@EntityScan(basePackages = { "com.example.entity" })
@EnableJpaRepositories(basePackages = { "com.example.repository" })
public class BhkTravelApplication {

	public static void main(String[] args) {
		SpringApplication.run(BhkTravelApplication.class, args);
		System.out.println("server start");
	}

}
