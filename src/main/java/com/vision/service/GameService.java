package com.vision.service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.vision.entity.GameCategory;
import com.vision.entity.Games;
import com.vision.entity.GamesById;
import com.vision.entity.GamesData;
import com.vision.entity.Winners;
import com.vision.entity.WinnersList;


@Service
public class GameService {

	@Autowired
	private RestTemplate restTemplate;

	public GameCategory getGameCategory() {

		GameCategory gameCategories = restTemplate
				.getForObject("https://games.gamepix.com/categories?sid=41151&order=d", GameCategory.class);
//		System.out.println(gameCategories);
		Collections.sort(gameCategories.getData());

		return gameCategories;
	}

	public List<GamesData> getCategoryWiseGAme() {
		List<GamesData> catwisegame = getGameCategory().getData().stream().map(cate -> {
			GamesData gamedata = restTemplate.getForObject("https://games.gamepix.com/games?sid=41151&order=d&category=" + cate.getId(), GamesData.class);
			
//			System.out.println("GameData::::::"+gamedata);
			
//			gamedata.setCategoryid(cate.getName());
//			gamedata.setCategoryid(cate.getName_en());
			
//			System.out.println("cateName:::: "+cate.getName());
//			System.out.println("catNameEn::::"+cate.getName_en());
			
			gamedata.setCategoryid(cate.getId());
			gamedata.setCategoryname(cate.getName());
			return gamedata;
			
		}).collect(Collectors.toList());

		List<GamesData> catwgame = new ArrayList<GamesData>();
		ArrayList<String> duplicate = new ArrayList<String>();
		for (GamesData games : catwisegame) {
			
//			System.out.println(""+games.getCategoryname());
			
			catwgame.add(
					new GamesData(games.getStatus(), games.getCode(), games.getCategoryid(), games.getCategoryname(),
							games.getData().stream().filter(gg -> !duplicate.contains(gg.getId())).map(g -> {
								duplicate.add(g.getId());
								return g;
							}).collect(Collectors.toList())));
			
//			System.out.println("category is "+games.getCategoryname());
//			System.out.println("game is "+games.getData());
			
			List<Games> data = games.getData();
			for(Games g : data)
			{
				g.getTitle();
//				System.out.println(g.getTitle());
			}
			
		}

//		System.out.println("category wise game "+catwgame);
		
		return catwgame;

	}

	public GamesData getGamesByCatID(String catid) {
		return restTemplate.getForObject("https://games.gamepix.com/games?sid=41151&category=" + catid,
				GamesData.class);

	}

	public GamesById getGameById(String id) {
		id = id == null ? "9PF8M" : id;
		return restTemplate.getForObject("https://games.gamepix.com/game?sid=41151&gid=" + id, GamesById.class);
	}
	
	public  WinnersList getDailyWinner()
	{
		WinnersList winnerlist=new WinnersList();
		try {
			 Winners[] winners = restTemplate.getForObject("https://api.ydplatform.com/zw/getwinners.ashx",Winners[].class);

			 Integer winnerSize=0;
			 
			 LocalDate yesterday = LocalDate.now().minusDays(1);
			 System.out.println("yesterday is "+yesterday);
			 
			 for (Winners winner: winners) {
				int datediff = LocalDate.now().compareTo( winner.getWinning_date());
					if(datediff == 0){
					 winnerlist.setTotalwinners(String.valueOf(winner.getWinners().size()));
					 winnerlist.setWinner_number(( new StringBuilder(winner.getWinning_msisdn().startsWith("263") ? winner.getWinning_msisdn().substring("263".length()) : winner.getWinning_msisdn())).toString().split("") );
					 winnerlist.setWinning_date(winner.getWinning_date().toString());
				 }else if(datediff == 1){
						 winnerlist.setTotalwinners(String.valueOf(winner.getWinners().size()));
						 winnerlist.setWinner_number(( new StringBuilder(winner.getWinning_msisdn().startsWith("263") ? winner.getWinning_msisdn().substring("263".length()) : winner.getWinning_msisdn())).toString().split("") );
						 winnerlist.setWinning_date(winner.getWinning_date().toString());
					 }	
					
					//Getting Yesterday Winners Count (added by @shu)
					if(winner.getWinning_date().equals(yesterday))
					{
						 winnerSize=winnerSize+winner.getWinners().size();
						 System.out.println(winnerSize);
						 winnerlist.setTotalwinners(String.valueOf(winnerSize));
					}
			}
			System.out.println("winner size is "+winnerSize);
		return winnerlist;
								
		
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
			return winnerlist;
		}
	}
}
