package com.dtw.demoSpringBoot;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.dtw.demoSpringBoot.utils.DtoToPersonConverter;
import com.dtw.demoSpringBoot.utils.PersonToDtoConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class DemoSpringBootApplication implements WebMvcConfigurer {
	
	public static void main(String[] args) {
		SpringApplication.run(DemoSpringBootApplication.class, args);
	}
	
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new PersonToDtoConverter());
		registry.addConverter(new DtoToPersonConverter());
	}

	@Bean
	public ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}
	
	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public Validator getValidatorFactory() {
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		return factory.getValidator();
	}
}