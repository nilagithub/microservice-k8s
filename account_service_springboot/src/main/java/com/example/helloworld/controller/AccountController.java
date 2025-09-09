package com.example.helloworld.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
	final Logger logger = LoggerFactory.getLogger(AccountController.class);    
	
	@GetMapping("/account")
    public String getAccount() {    
		logger.info("****inside getAccount info**********");
        return "Hello, Account Service (Springboot) is UP & Running.";
    }
    
    @GetMapping("/account/header")
    public String getHeader(@RequestHeader("Test-Header") String authHeader) {
    	logger.info("****inside getHeader info**********");
    	return "Hello, Account Service (Springboot) got auth header: "+authHeader;
    }
}
