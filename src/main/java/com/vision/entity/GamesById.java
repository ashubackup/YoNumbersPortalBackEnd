package com.vision.entity;


import lombok.*;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class GamesById {
	
	private String status;
	private String code;
	private Games data;


}
