package com.vision.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.vision.service.UnsubscriptionService;

@RestController
@CrossOrigin("*")
public class UnsubscriptionController {
	@Autowired
	private UnsubscriptionService service;
	
	
	@GetMapping("/unsubscription/{ani}")
	public ResponseEntity<?> unsubUser(@PathVariable("ani") String ani) {
		try {
			service.unsubscriptionProcess(ani);  
		    return new ResponseEntity<>("Success", HttpStatus.OK);
			
		}catch(Exception e) {
			e.getMessage();
			 return new ResponseEntity<>("Failed", HttpStatus.OK);

		}
	    
	}

}
