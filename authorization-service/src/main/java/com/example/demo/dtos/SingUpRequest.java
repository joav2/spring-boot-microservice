package com.example.demo.dtos;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SingUpRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String fullname,
        @NotBlank String email
) {
}
