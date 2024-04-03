package com.marcos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marcos.controller.dto.AuthRequestDTO;
import com.marcos.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private AuthService userDetailService;

	@Autowired
	public AuthController(AuthService userDetailService) {
		this.userDetailService = userDetailService;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody @Valid AuthRequestDTO request) {
		try {
			return new ResponseEntity<>(userDetailService.createUser(request), HttpStatus.CREATED);
		} catch (Exception e) {
			 return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid AuthRequestDTO request) {
		try {
			return new ResponseEntity<>(userDetailService.loginUser(request), HttpStatus.OK);
		} catch (Exception e) {
			 return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
