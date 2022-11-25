package com.example.demo.dtos;

import java.util.List;

import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(Integer httpStatus, 
		String excepiton, 
		String message, 
		List<FieldError> fieldsErrors) {
}
