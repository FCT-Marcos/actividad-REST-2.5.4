package com.marcos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/page")
public class TestController {

	@GetMapping("/public")
	public String getPublic() {
		return "Public page";
	}
	
	@GetMapping("/protected")
	public String getProtected() {
		return "Protected page";
	}
}
