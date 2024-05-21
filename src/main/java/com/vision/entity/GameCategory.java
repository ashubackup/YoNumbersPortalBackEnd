package com.vision.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GameCategory {
	
//	private int id;
	private String status;
	private String code;
	private List<Categories> data;
	
	

}
