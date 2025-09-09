package com.example.demogateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

	@RequestMapping("/account/fallback")
    public ResponseEntity<String> fallback() {
        return new ResponseEntity<>("Account Service is temporarily unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }
	@RequestMapping("/auth/fallback")
    public ResponseEntity<String> authFallback() {
        return new ResponseEntity<>("Auth Service is temporarily unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }
	@RequestMapping("/backend/fallback")
    public ResponseEntity<String> backendFallback() {
        return new ResponseEntity<>("Backend Service is temporarily unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
