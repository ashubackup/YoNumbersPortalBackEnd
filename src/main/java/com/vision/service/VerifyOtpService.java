package com.vision.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vision.entity.Request;
import com.vision.entity.Response;
import com.vision.entity.TblOtp;
import com.vision.repository.TblOtpRepository;

@Service
public class VerifyOtpService {
	@Autowired
	private TblOtpRepository otpRepo;
	
	public Response validateOtp(Request request)
	{
		try {
			
			TblOtp otp = otpRepo.findByAni(request.getAni());
			if(otp==null)
			{
				return Response.builder()
						.statusCode("400")
						.message("You have not sent an otp")
						.code("3")		
						.build();
			}
			
			if(otp.getExpire_At().isBefore(LocalDateTime.now()))
			{
				return Response.builder()
						.statusCode("400")
						.message("Expire Otp")
						.code("3")
						.build();
				
			}
			
			if(!otp.getOtp().equals(request.getOtp()))
			{
				return Response.builder()
						.statusCode("400")
						.message("Invalid Otp")
						.code("3")
						.build();
				
			}
			
			
			return Response.builder()
					.statusCode("200")
					.message("Success")
					.code("1")
					.build();			
			
		}catch(Exception e) {
			e.printStackTrace();
			return Response.builder()
					.statusCode("400")
					.message("Expire Otp")
					.code("3")
					.build();
		}
	}
}
