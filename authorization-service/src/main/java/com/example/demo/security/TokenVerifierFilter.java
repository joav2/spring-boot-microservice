package com.example.demo.security;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.services.TokenEntityService;

@Component
public class TokenVerifierFilter extends OncePerRequestFilter {
	
	private final UserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;
    private final TokenEntityService tokenEntityService;

	public TokenVerifierFilter(UserDetailsService userDetailsService, TokenProvider tokenProvider,
			TokenEntityService tokenEntityService) {
		super();
		this.userDetailsService = userDetailsService;
		this.tokenProvider = tokenProvider;
		this.tokenEntityService = tokenEntityService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		getJwtFromRequest(request)
        .map(tokenProvider::getClaimFromToken)
        .ifPresent(jws -> {
        	ApplicationUserDetails applicationUserDetails = (ApplicationUserDetails) userDetailsService.loadUserByUsername(jws.getBody().getSubject());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(applicationUserDetails, null, applicationUserDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String username = jws.getBody().getSubject();
            List<String> grantedAuthorities = (List<String>) jws.getBody().get("rol");
      
            request.setAttribute("username", username);
            request.setAttribute("authorities", grantedAuthorities);
            request.setAttribute("jwt", jws.getSignature());
        });
		
		filterChain.doFilter(request, response);
	}
	
	private Optional<String> getJwtFromRequest(HttpServletRequest request) {
        String tokenHeader = request.getHeader(TOKEN_HEADER);
        if (StringUtils.hasText(tokenHeader) && tokenHeader.startsWith(TOKEN_PREFIX)) {
            return getTokenFromTokenHeader(Optional.of(tokenHeader.replace(TOKEN_PREFIX, "")));
        }
        return Optional.empty();
    }
	
	private Optional<String> getTokenFromTokenHeader(Optional<String> tokenHeader) {
		Optional<String> token = tokenHeader
				.map(tokenEntityService::findById)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
				.map(t -> t.getAuthenticationToken());
		return token;
	}

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
}
