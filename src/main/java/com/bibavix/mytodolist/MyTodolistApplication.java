package com.bibavix.mytodolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = { "com.bibavix" })
@EnableJpaRepositories(basePackages = "com.bibavix.repository")
@EntityScan(basePackages = "com.bibavix.model")
public class MyTodolistApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyTodolistApplication.class, args);
	}

}
