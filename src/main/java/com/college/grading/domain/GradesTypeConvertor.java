package com.college.grading.domain;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GradesTypeConvertor implements Converter<String, GradesType> {

	@Override
	public GradesType convert(String source) {
		
		return GradesType.valueOf(source);
	}

	
}
