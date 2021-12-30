package com.example.bhk_travel;

import org.mybatis.spring.annotation.MapperScan;
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
@ComponentScan(basePackages = { "com.example.restcontroller", "com.example.security", "com.example.jwt",
		"com.example.service", "com.example.mybatis" })
@MapperScan(basePackages = { "com.example.mappers" })
@EntityScan(basePackages = { "com.example.entity" })
@EnableJpaRepositories(basePackages = { "com.example.repository" })
@EnableScheduling
public class BhkTravelApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BhkTravelApplication.class, args);
		System.out.println("server start");

	}

	/*
	SpringBootServletInitializer을 상속받아 configure()을 재정의
	WAS에 war 파일을 배포할 때 Spring Boot의 내용을 Servlet으로 초기화(initialize)해주는 역할
	기존의 Jar 프로젝트로 생성했을 경우 일반 애플리케이션용 혹은 내장 WAS을 이용해 웹 서버를 실행하고 서비스를 제공하는 형태인데 반해,
	War 프로젝트로 생성할 경우 위와 같이 WAS에 배포(deply)될 때 Spring Boot 애플리케이션을 Servlet으로 등록하여
	서비스될 수 있도록 제공함
	*/
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(BhkTravelApplication.class);
	}

}
