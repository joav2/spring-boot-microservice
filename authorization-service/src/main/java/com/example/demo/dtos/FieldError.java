package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FieldError(
		String field, 
		String errorCode, 
		String message) {
}
