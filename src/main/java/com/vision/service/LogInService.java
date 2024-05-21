package com.vision.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vision.entity.TableSubscription;
import com.vision.entity.TblPriceInfo;
import com.vision.entity.Request;
import com.vision.entity.Response;
import com.vision.repository.TblPriceInfoRepo;
import com.vision.repository.TblSubscriptionRepo;
import com.vision.util.BillingServiceNew;
import com.vision.util.OtpSendService;


@Service
public class LogInService {
	@Autowired
	private TblSubscriptionRepo subRepo;
	@Autowired
	private OtpSendService otpSendService;
	@Autowired
	private BillingServiceNew billingService;
	@Autowired
	private TblPriceInfoRepo priceInfoRepo;
	
	
	private static final String STATUS_CODE_SUCCESS = "200";
    private static final String CODE_ACCESS = "1";
    private static final String CODE_BILLING_PENDING = "2";
    private static final String CODE_NOT_SUBSCRIBER = "0";
	
	public Response authenticate(Request request)
	{
		try {
			String ani = request.getAni();
			ani = ani.startsWith("0")?ani.substring("0".length()):ani;
			ani = ani.startsWith("263")?ani.substring("263".length()):ani;
			
			System.out.println("Ani is--"+ani);
			TableSubscription sub = subRepo.findByAni(ani);
			if (sub == null) {
                return buildResponse(STATUS_CODE_SUCCESS, "Not a Subscriber", CODE_NOT_SUBSCRIBER);
            }
			
			if (sub.getNext_billed_date() == null || !canAccess(sub.getNext_billed_date())) {
                return handleBilling(sub);
            }
			return sendOtpAndBuildResponse(ani);
			
			
			
			
			
			
			
//			if(sub!=null)
//			{
//				if(sub.getNext_billed_date()!=null)
//				{
//					if(sub.getNext_billed_date().isAfter(LocalDateTime.now())
//							|| sub.getNext_billed_date().equals(LocalDateTime.now()))
//					{
//						//User Can Access
//						//sent otp for two step verification
//						System.out.println("User Can Access");
//						String otp = otpSendService.generateAndStoreOtp(ani);
//						System.out.println("Otp is " + otp);
//						otpSendService.sendOTP(request.getAni(), otp);
//						
//						return Response.builder()
//								.statusCode("200")
//								.message("User Can Access, Navigate to Otp verify page")
//								.code("1")		
//								.build();
//					}
//					
//					//User Can't Access
//					//Go for billing
//					//If success send otp if failed show Billing Pending
//					System.out.println("User Can't Access go for billing");
//					
//					TblPriceInfo info = priceInfoRepo.findPriceByPack(sub.getPack());		
//					String responseCode = billingService.hitBilling(sub.getAni(),sub.getPack(), info.getPrice(), info.getOffercode());								//String responseCode="0";
//					if(responseCode.equalsIgnoreCase("0"))
//					{
//						//billing success 
//						String otp = otpSendService.generateAndStoreOtp(sub.getAni());
//						System.out.println("Otp is " + otp);
//						otpSendService.sendOTP(request.getAni(), otp);
//						
//						return Response.builder()
//								.statusCode("200")
//								.message("User Can Access, Navigate to Otp verify page")
//								.code("1")		
//								.build();
//						
//					}
//					
//					return Response.builder()
//							.statusCode("200")
//							.message("Billing Pending")
//							.code("2")		
//							.build();
//						
//						
//					
//				}
//				return Response.builder()
//						.statusCode("200")
//						.message("Billing Pending")
//						.code("2")		
//						.build();
//			}
//			return Response.builder()
//					.statusCode("200")
//					.message("Not a Subscriber")
//					.code("0")		
//					.build();
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return buildResponse(STATUS_CODE_SUCCESS, "Billing Pending", CODE_BILLING_PENDING);
		}
	}
	
	private Response sendOtpAndBuildResponse(String ani) {
	        System.out.println("User Can Access");
//	        String otp = otpSendService.generateAndStoreOtp(ani);
//	        System.out.println("Otp is-- " + otp);
	        otpSendService.sendOTP(ani);
	        
	        return buildResponse(STATUS_CODE_SUCCESS, "User Can Access, Navigate to Otp verify page", CODE_ACCESS);
    }
	
	
	private Response handleBilling(TableSubscription sub) 
	{
        System.out.println("User Can't Access go for billing");
        TblPriceInfo info = priceInfoRepo.findPriceByPack(sub.getPack());
        String responseCode = billingService.hitBilling(sub.getAni(), sub.getPack(), info.getPrice(), info.getOffercode());

        if ("0".equalsIgnoreCase(responseCode)) {
            return sendOtpAndBuildResponse(sub.getAni());
        }
        
        return buildResponse(STATUS_CODE_SUCCESS, "Billing Pending", CODE_BILLING_PENDING);
    }
	
	
	private boolean canAccess(LocalDateTime nextBilledDate) {
        return !nextBilledDate.isBefore(LocalDateTime.now());
    }
	
	
	private Response buildResponse(String statusCode, String message, String code) {
        return Response.builder()
                .statusCode(statusCode)
                .message(message)
                .code(code)
                .build();
    }

}
