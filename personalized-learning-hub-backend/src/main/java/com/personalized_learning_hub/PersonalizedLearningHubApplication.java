package com.personalized_learning_hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
@ComponentScan(basePackages = "com.personalized_learning_hub")
public class PersonalizedLearningHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonalizedLearningHubApplication.class, args);
	}

}
