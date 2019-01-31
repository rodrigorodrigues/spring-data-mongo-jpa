package com.example.demo;

import com.example.demo.dto.PersonDto;
import com.example.demo.service.PersonService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.demo.repository.jpa")
@EnableMongoRepositories(basePackages = "com.example.demo.repository.mongo")
public class DemoApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner insertOnLoad(PersonService personService) {
		return args -> {
			personService.createPerson(PersonDto.builder().name("Admin").build());

			personService.createPerson(PersonDto.builder().name("Anonymous").build());

			personService.createPerson(PersonDto.builder().name("Test").build());
		};
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/api/persons");
	}
}

