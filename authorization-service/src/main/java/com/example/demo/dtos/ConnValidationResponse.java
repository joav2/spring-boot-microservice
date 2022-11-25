package com.example.demo.dtos;

import java.util.List;

public record ConnValidationResponse(
			String status,
			boolean isAuthenticated,
			String methodType,
			String username,
			String token,
			List<String> authorities
		) {

}