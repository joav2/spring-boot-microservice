package com.example.demo.web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.ConnValidationResponse;

@RestController
@RequestMapping("/api/v1/validateToken")
public class ConnectionValidatorController {

	@GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ConnValidationResponse> validateGet(HttpServletRequest request) {
		String username = (String) request.getAttribute("username");
		String token = (String) request.getAttribute("jwt");
		List<String> grantedAuthorities = (List<String>) request.getAttribute("authorities");

		return ResponseEntity.ok(new ConnValidationResponse("OK", true, HttpMethod.GET.name(), username, token, grantedAuthorities));
	}
}
