package com.example.demo.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.UserDTO;
import com.example.demo.services.UserService;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class ApplicationUserDetailsService implements UserDetailsService{

	private final UserService userService;
	
	public ApplicationUserDetailsService(UserService userService) {
		super();
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDTO userDTO = userService.findByUsername(username);
		if (userDTO == null) throw new UsernameNotFoundException("Username Not Found");
		
		List<SimpleGrantedAuthority> authorities = userDTO.roles()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
		
		return mapToUserDetails(userDTO, authorities);
	}
	
	private ApplicationUserDetails mapToUserDetails(final UserDTO userDTO, final List<SimpleGrantedAuthority> authorities) {
		return new ApplicationUserDetails(userDTO.id(), userDTO.username(), userDTO.password(), userDTO.email(), authorities);
	}

}
