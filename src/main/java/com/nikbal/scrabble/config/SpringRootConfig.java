package com.nikbal.scrabble.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan({ "com.nikbal.scrabble" })
@Configuration
public class SpringRootConfig {
	@Autowired
	@Qualifier("EmbeddedDataSource")
	private DataSource dataSource;
}
