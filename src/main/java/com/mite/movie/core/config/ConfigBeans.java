package com.mite.movie.core.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class used as a source of bean definitions.
 * 
 * @author Mykhaylo.T
 */
@Configuration
public class ConfigBeans {
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
