package com.example.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.security.TokenVerifierFilter;

@EnableWebSecurity
public class WebSecurityConfig {
	
	private final TokenVerifierFilter tokenVerifierFilter;

    public WebSecurityConfig(TokenVerifierFilter tokenVerifierFilter) {
		super();
		this.tokenVerifierFilter = tokenVerifierFilter;
	}

	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests((authz) ->
                authz
                        .antMatchers("/public/**", "/auth/**", "/api/v1/validateToken").permitAll()
                        .anyRequest().authenticated());
        http.addFilterBefore(tokenVerifierFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        http.cors().and().csrf().disable();
        return http.build();
    }
}