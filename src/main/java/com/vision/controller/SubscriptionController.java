package com.vision.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vision.entity.Request;
import com.vision.entity.Response;
import com.vision.service.SubscriptionService;

@RestController
@CrossOrigin("*")
public class SubscriptionController {
	@Autowired
	private SubscriptionService service;
	
	@PostMapping("/subscribe")
	public ResponseEntity<Response> subscribeUserFromWeb(@RequestBody Request request)
	{
		return ResponseEntity.ok(service.subscribeUser(request));
	}
	@GetMapping("/price")
	public ResponseEntity<?> sendPrice()
	{
		return ResponseEntity.ok(service.sendAllPrice());
	}
	
    @GetMapping("/getDetails/{ani}")
	public ResponseEntity<?> sendUserDetails(@PathVariable String ani)
	{
    	ani = ani.startsWith("0")?ani.substring("0".length()):ani;
		ani = ani.startsWith("263")?ani.substring("263".length()):ani;
		try {
			return ResponseEntity.ok(service.getUserDetails(ani));
		}catch(Exception e) {
			return new ResponseEntity<String>("Not Found!", HttpStatus.NOT_FOUND);
		}
	}
}
