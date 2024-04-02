package com.marcos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import com.marcos.controller.dto.AuthRequest;
import com.marcos.controller.dto.AuthResponse;
import com.marcos.util.JwtUtils;

@Service
public class UserService {
	
	private final PasswordEncoder passwordEncoder;
	private final InMemoryUserDetailsManager userDetailsManager;
	private final JwtUtils jwtUtils;
	
	@Autowired
	public UserService(PasswordEncoder passwordEncoder, InMemoryUserDetailsManager userDetailsManager, JwtUtils jwtUtils) {
		super();
		this.passwordEncoder = passwordEncoder;
		this.userDetailsManager = userDetailsManager;
		this.jwtUtils = jwtUtils;
	}
	
	public AuthResponse createUser(AuthRequest request) {
		String username = request.username();
		String password = request.password();
		UserDetails userDetails = User.withUsername(username)
				.password(passwordEncoder.encode(password))
				.roles("USER")
				.authorities("READ")
				.build();
		
		userDetailsManager.createUser(userDetails);
		Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
		String accessToken = jwtUtils.createToken(authentication);
		
		return new AuthResponse(username, "User created successfully", accessToken);
	}
	
	public AuthResponse loginUser(AuthRequest request) {
		String username = request.username();
		String password = request.password();
		
		Authentication authentication = this.authenticate(username, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String accessToken = jwtUtils.createToken(authentication);
		return new AuthResponse(username, "Successfully logged", accessToken);
	}
	
	public Authentication authenticate(String username, String password) {
        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect Password");
        }

        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }



}
