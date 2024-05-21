package com.vision.entity;

import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GamesData {
	
	
	private String status;
	private String code;
	private String categoryid;
	private String categoryname;
	private List<Games> data;

}
