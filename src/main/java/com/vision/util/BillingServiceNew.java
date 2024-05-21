package com.vision.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vision.entity.TableSubscription;
import com.vision.entity.TblBillingLogs;
import com.vision.entity.TblBillingSuccess;
import com.vision.model.JsonRequest;
import com.vision.model.RequestParam;
import com.vision.repository.TblBillingLogsRepo;
import com.vision.repository.TblBillingSuccessRepo;
import com.vision.repository.TblSubscriptionRepo;

@Service
public class BillingServiceNew {

	@Value("${tokenApiUsername}")
	private String userName;

	@Value("${tokenApiPassword}")
	private String password;

	@Value("${billingApi}")
	private String subUrl;

	@Autowired
	private TblBillingSuccessRepo billingSuccessRepo;
	
	@Autowired
	private TblSubscriptionRepo subscriptionRepo;

	@Autowired
	private Api api;
	
	@Autowired
	private TblBillingLogsRepo billingLogsRepo;
	
	public String hitBilling(String ani, String pack, String price, String offerCode) 
	{
		try {
			
			String random16DigitNumber = new SecureRandom().ints(16, 0, 10).mapToObj(Integer::toString)
					.collect(java.util.stream.Collectors.joining());

			LocalDateTime timeStamp = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

			String encryptKey = generateKey(password);

			LocalDateTime nextBilledDateTime = LocalDateTime.now();

			if (pack.equalsIgnoreCase("LGAMING_D")) {
				nextBilledDateTime = nextBilledDateTime.plusDays(1);
			} else if (pack.equalsIgnoreCase("LGAMING_W")) {
				nextBilledDateTime = nextBilledDateTime.plusWeeks(1);
			} else if (pack.equalsIgnoreCase("LGAMING_M")) {
				nextBilledDateTime = nextBilledDateTime.plusMonths(1);
			} else {
				nextBilledDateTime = nextBilledDateTime.plusDays(1);
			}

			JsonRequest json = new JsonRequest();
			json.setRequestId(random16DigitNumber);
			json.setChannel("2");
			json.setRequestTimeStamp(timeStamp.toString());
			json.setSourceNode("SourceNode"); // partner_Tunnel
			json.setSourceAddress("91.205.172.123");
			json.setFeatureId("Payment");
			json.setUsername(userName);
			json.setPassword(encryptKey);
			json.setExternalServiceId(ani);
			RequestParam param = new RequestParam();
			// planiD
			param.setSubscriptionOfferID(offerCode);
			param.setCpId("124");
			param.setChargeAmount(price);
			json.setRequestParam(param);

			ObjectMapper mapper = new ObjectMapper();
			String jsonValue = mapper.writeValueAsString(json);
			System.out.println("Json Vlaue" + jsonValue);

			String resultCode = api.hitBillingApi(ani, jsonValue);
			if (resultCode.equalsIgnoreCase("0"))
			{
				saveService(ani,nextBilledDateTime,offerCode,price,pack);
				api.sendToOren(ani, price, pack,"subscribe");
				
			}
			return resultCode;
		} catch (Exception e) {
			e.printStackTrace();
			return "1";
		}
	}

	

	public void saveService(String ani, LocalDateTime nextBilledDate,String offerCode, String price, String pack) {
		try {
			LocalDateTime dateTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");

	        // Format the LocalDateTime object using the formatter
	        String formattedDatetime = dateTime.format(formatter);
			
	        TableSubscription subscriber = subscriptionRepo.findByAni(ani);
	        if(subscriber == null) {
	        	subscriptionRepo.save(TableSubscription.builder()
						.ani(ani)	
						.billing_date(dateTime)
						.default_amount(price)
						.lang("en")
						.m_act("WEB")
						.offercode(offerCode)
						.pack(pack)
						.service("games")
						.RECORDSTATUS("1")				
						.sub_date_time(dateTime)
						.last_billed_date(formattedDatetime)
						.next_billed_date(nextBilledDate)
						.build());
	        	
	        }else {
	        	subscriber.setLast_billed_date(formattedDatetime);
	        	subscriber.setNext_billed_date(nextBilledDate);     
	        	subscriber.setPack(pack);       
	        	subscriber.setDefault_amount(price);
	        	subscriptionRepo.save(subscriber);
	        
	        }
	        
			
			
			billingSuccessRepo.save(TblBillingSuccess.builder()
					.ani(ani)
					.datetime(dateTime)
					.total_amount(price)
					.deducted_amount(price)
					.type_event("SUB")
					.servicename("games")
					.subservicename("games")
					.errordesc("ONLINE CHARGING SUCCESS")
					.pack_type(pack)
					.process_datetime(dateTime)
					.recordstatus("1")
					.build());

			
			billingLogsRepo.save(TblBillingLogs.builder()	
					.ani(ani)	
					.datetime(dateTime)
					.total_amount(price)
					.deducted_amount(price)
					.type_event("SUB")
					.mode("WEB")
					.servicename("games")
					.subservicename("games")
					.errordesc("ONLINE CHARGING SUCCESS")
					.pack_type(pack)
					.process_datetime(dateTime)
					.recordstatus("1")
					.product_id("1")	
					.build());	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String generateKey(String text) {
		 try {
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            byte[] md5Hash = md.digest(text.getBytes());

	            // Convert the byte array to a fixed-length 32-character hexadecimal string
	            StringBuilder hexString = new StringBuilder(32);
	            for (byte b : md5Hash) {
	                String hex = String.format("%02x", b);
	                hexString.append(hex);
	            }

	            return hexString.toString();
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	            return "Error: " + e.getMessage();
	        }
	}
}
