package com.codingz2m.savingsaccountservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SavingsAccountServiceConfig {
	 
	 @Bean
	 public ModelMapper getModelMapper() {
		return new ModelMapper(); 
	 }
	 
}
