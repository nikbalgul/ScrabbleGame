package com.nikbal.scrabble.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.nikbal.scrabble.interceptor.LoggerInterceptor;

@EnableWebMvc
@Configuration
@ComponentScan("com.nikbal.scrabble")
public class SpringMvcConfig extends WebMvcConfigurerAdapter {

	@Bean
	LoggerInterceptor demoInterceptor() {
		return new LoggerInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(demoInterceptor());
	}

}
