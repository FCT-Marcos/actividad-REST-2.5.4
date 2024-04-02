package com.marcos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marcos.controller.dto.AuthRequest;
import com.marcos.controller.dto.AuthResponse;
import com.marcos.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private UserService userDetailService;

	@Autowired
	public AuthController(UserService userDetailService) {
		this.userDetailService = userDetailService;
	}

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthRequest request) {
		return new ResponseEntity<>(userDetailService.createUser(request), HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
		return new ResponseEntity<>(userDetailService.loginUser(request), HttpStatus.OK);
	}
}
