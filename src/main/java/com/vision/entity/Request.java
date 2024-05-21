package com.vision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
	private String ani;
	private String otp;
	private String packType;
	private String status;
	private String message;
	

}
