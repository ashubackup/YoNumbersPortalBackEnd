package com.vision.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.vision.entity.GameCategory;
import com.vision.service.GameService;


@RestController
@CrossOrigin("*")
public class GameSendController {
	@Autowired
	private GameService gameService;

	@GetMapping("/getGames")
	public ResponseEntity<Map<String, Object>> sendGamesCategoryAndWinners() {
		
		try {
			Map<String, Object> model = new HashMap<>();
			
	    	model.put("games", gameService.getCategoryWiseGAme());
			model.put("category", gameService.getGameCategory());
			model.put("list" ,new ArrayList<String>());
			model.put("winners",gameService.getDailyWinner());
			
			System.out.println("winners "+gameService.getDailyWinner());
			return ResponseEntity.ok(model);
			
		}catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	@GetMapping("/play/{id}")
	public ResponseEntity<Map<String, Object>> playGame(@PathVariable(required = false) String id) 
	{
		try {
	        if (id == null || id.isEmpty()) {
	            return ResponseEntity.badRequest().build();
	        }

	        Map<String, Object> model = new HashMap<>();
	        model.put("gameurl", gameService.getGameById(id));
	        return ResponseEntity.ok(model);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	
	@GetMapping("/bycat/play/{id}")
	public ResponseEntity<Map<String, Object>> playGame2(@PathVariable(required = false) String id) 
	{
		try {
			 if (id == null || id.isEmpty()) {
		            return ResponseEntity.badRequest().build();
		        }
			Map<String, Object> model = new HashMap<>();
			model.put("gameurl", gameService.getGameById(id));
			return ResponseEntity.ok(model);
		}catch(Exception e) {
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
		
	}
	

	@GetMapping("/bycat/{catid}")
	public ResponseEntity<Map<String, Object>> gamesByCatID(@PathVariable(required = false) String catid )
	{  
		try {
			Map<String, Object> model = new HashMap<>();
			GameCategory gameCategory = gameService.getGameCategory();
			if( catid == null) {
				catid =gameCategory.getData().get(0).getId();
			}
			String cati=catid;
			
		    Set<String> catname = gameCategory.getData().stream().filter(cat->cat.getId().equalsIgnoreCase(cati)).map(e -> e.getName()).collect(Collectors.toSet());
			 
			model.put("categoryname", catname);
			model.put("bycat", gameService.getGamesByCatID(cati));
			model.put("category", gameCategory);

			return ResponseEntity.ok(model);
		}catch(Exception e) {
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}

}
