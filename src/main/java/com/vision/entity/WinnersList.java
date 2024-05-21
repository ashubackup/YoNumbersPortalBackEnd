package com.vision.entity;

import java.time.LocalDate;

import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WinnersList {
	
	private String[] winner_number= {"7","7","7"};
	private String winning_date=LocalDate.now().toString();
	private String totalwinners="0";

}
