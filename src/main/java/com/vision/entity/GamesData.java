package com.vision.entity;

import java.io.Serializable;
import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GamesData implements Serializable{
	

	private static final long serialVersionUID = 1L;
	private String status;
	private String code;
	private String categoryid;
	private String categoryname;
	private List<Games> data;

}
