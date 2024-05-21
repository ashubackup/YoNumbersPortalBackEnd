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
    private static final String STATUS_CODE_SUCCESS = "200";
	private static final String STATUS_CODE_FAILED = "400";
    private static final String CODE_ACCESS = "1";
    private static final String CODE_FAILED_ACCESS = "3";
	
	public Response validateOtp(Request request)
	{
		try {
			
			TblOtp otp = otpRepo.findByAni(request.getAni());
			if(otp==null)
			{
				return buildResponse(STATUS_CODE_FAILED,"You have not sent an otp!",CODE_FAILED_ACCESS);
			}
			
			if(otp.getExpire_At().isBefore(LocalDateTime.now()))
			{
				return buildResponse(STATUS_CODE_FAILED,"Expire Otp!",CODE_FAILED_ACCESS);
			}
			
			if(!otp.getOtp().equals(request.getOtp()))
			{
				return buildResponse(STATUS_CODE_FAILED,"Invalid Otp!",CODE_FAILED_ACCESS);
			}
			
			return buildResponse(STATUS_CODE_SUCCESS,"Success",CODE_ACCESS);
			
		}catch(Exception e) {
			e.printStackTrace();
			return buildResponse(STATUS_CODE_FAILED,"Expire Otp!",CODE_FAILED_ACCESS);
		}
	}
	
	public Response buildResponse(String statusCode, String message, String code)
	{
		return Response.builder()
				.statusCode(statusCode)
				.message(message)
				.code(code)
				.build();
	}
	
}
