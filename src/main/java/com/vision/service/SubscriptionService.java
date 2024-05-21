package com.vision.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vision.entity.Request;
import com.vision.entity.Response;
import com.vision.entity.TableSubscription;
import com.vision.entity.TblPriceInfo;
import com.vision.repository.TblPriceInfoRepo;
import com.vision.repository.TblSubscriptionRepo;
import com.vision.util.BillingServiceNew;
import com.vision.util.OtpSendService;

@Service
public class SubscriptionService {
	
	@Autowired
	private TblSubscriptionRepo subRepo;
	@Autowired
	private OtpSendService otpSendService;
	@Autowired
	private BillingServiceNew billingService;
	@Autowired
	private TblPriceInfoRepo priceInfoRepo;
	
	public Response subscribeUser(Request request)
	{
	    try {
	    	String ani = request.getAni();
			ani = ani.startsWith("0")?ani.substring("0".length()):ani;
			ani = ani.startsWith("263")?ani.substring("263".length()):ani;
	        TableSubscription sub = subRepo.findByAni(ani);
	        if(sub == null)
	        {
	            System.out.println("New subscriber");
	            String pack = "LGAMING_D";
	            if(request.getPackType().equalsIgnoreCase("Daily")) {
	                pack = "LGAMING_D";
	            }
	            if(request.getPackType().equalsIgnoreCase("Weekly")) {
	                pack = "LGAMING_W";
	            }
	            if(request.getPackType().equalsIgnoreCase("Monthly")) {
	                pack = "LGAMING_M";
	            }

	            TblPriceInfo info = priceInfoRepo.findPriceByPack(pack);
	           // String responseCode = billingService.hitBilling(ani, pack, price);
	            
	            String responseCode = billingService.hitBilling(ani, pack, info.getPrice(), info.getOffercode());
	            
	            
	           // String responseCode="1";
	            if(responseCode.equalsIgnoreCase("0"))
	            {
	                //billing success
	                otpSendService.sendOTP(ani);
	                

	                return Response.builder()
	                        .statusCode("200")
	                        .message("User Can Access, Navigate to Otp verify page")
	                        .code("1")
	                        .build();

	            }

	            return Response.builder()
	                    .statusCode("200")
	                    .message("Billing Pending")
	                    .code("2")
	                    .build();

	        } else {
	            if(sub.getNext_billed_date().isAfter(LocalDateTime.now())
	                    || sub.getNext_billed_date().equals(LocalDateTime.now()))
	            {
	                //User Can Access
	                //sent otp for two step verification
//	                System.out.println("User Can Access");
//	                String otp = otpSendService.generateAndStoreOtp(ani);
//	                System.out.println("Otp is " + otp);
	                otpSendService.sendOTP(ani);
	               

	                return Response.builder()
	                        .statusCode("200")
	                        .message("Already Subscribed, Send to otp verify Page")
	                        .code("1")
	                        .build();
	            } else {
	                return Response.builder()
	                        .statusCode("200")
	                        .message("Subscription Expired")
	                        .code("3")
	                        .build();
	            }
	        }

	    } catch(Exception e)
	    {
	        e.printStackTrace();
	        return Response.builder()
	                .statusCode("400")
	                .message("Billing Pending")
	                .code("2")
	                .build();
	    }
	}
	
	public List<Object[]> sendAllPrice()
	{
		return priceInfoRepo.getAllPrice();
	}
}



