package com.vision.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Games {
	
	
	private String id;
	private String title;
	private String creation;
	private String featured;
	private String height;
	private String width;
	private String thumbnailUrl;
	private String thumbnailUrl100;
	private String url;
	private String description;
	private String category;
 

}
