package com.example.demo.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello-world")
public class HelloWordController {

	@GetMapping
	public ResponseEntity<String> helloWorld() {
		return ResponseEntity.ok("Alo");
	}
}
