package com.vision.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vision.entity.Request;
import com.vision.entity.Response;
import com.vision.service.LogInService;
import com.vision.service.VerifyOtpService;


@RestController
@CrossOrigin("*")
public class AuthenticationController {
	@Autowired
	private LogInService logInService;
	@Autowired
	private VerifyOtpService verifyOtpService;
	
	
    @PostMapping("/authenticate")
    public ResponseEntity<Response> authenticate(@RequestBody Request request) {
        return ResponseEntity.ok(logInService.authenticate(request));
    }
    
    @PostMapping("/verifyOtp")
    public ResponseEntity<Response> verifyOtp(@RequestBody Request request) {
        return ResponseEntity.ok(verifyOtpService.validateOtp(request));
    }

}
