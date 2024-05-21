package com.vision.entity;

import java.time.LocalDate;
import java.util.List;

import lombok.*;
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Winners {
	
	private LocalDate winning_date ;
    private String winning_msisdn ;
    private String prize;
    private List<String> winners;

}
