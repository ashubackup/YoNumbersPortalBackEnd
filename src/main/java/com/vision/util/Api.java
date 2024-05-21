package com.vision.util;

import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.vision.entity.SendLogs;
import com.vision.repository.SendLogsRepo;

@Service
public class Api 
{
	
	@Autowired
	TokenApi tokenApi;
	
	@Value("${billingApi}")
	private String subUrl;
	@Autowired
	private SendLogsRepo sendLogsRepo;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public String hitBillingApi(String ani,String json)
	{
		try
		{
		
			String accessToken = tokenApi.generateToken();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Connection", "keep-alive");
            headers.add("X-Authorization","Bearer "+accessToken);
            headers.add("Content-Type", "application/json");
            headers.add("Accept-Encoding", "application/gzip");
            headers.add("Accept", "*/*");

            HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
	          
            System.out.println("The value of request"+requestEntity);
            ResponseEntity<String> responseEntity = restTemplate.exchange(subUrl, HttpMethod.POST, requestEntity, String.class);
            JSONObject responeVlue = new JSONObject(responseEntity);
	          
            String body=responeVlue.get("body").toString();
           
            System.out.println("Response is-- "+body);
            JSONObject jsonObject = new JSONObject(body);
            System.out.println("Result code : " + jsonObject.get("resultCode").toString());
            //0 means success
            return jsonObject.get("resultCode").toString();
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return "1";
		}
		
	}
	
	public void sendToOren(String ani,String amount,String pack,String type)
	{
		try
		{
			String serviceId="";
			if(pack.equalsIgnoreCase("LGAMING_D"))
			{
				serviceId="750";
			}
			else if(pack.equalsIgnoreCase("LGAMING_W"))
			{
				serviceId="760";
			}
			else if(pack.equalsIgnoreCase("LGAMING_M"))
			{
				serviceId="761";
			}
			
			String url ="https://api.ydplatform.com/zw/notification.ashx";
			JSONObject js = new JSONObject();
			js.put("msisdn", ani);
			js.put("amount_billed", amount);
			js.put("service_id", serviceId);
			js.put("action", type);
			
			System.out.println("\nRequest is "+js.toString());
			
			//Sending to Oren
			ResponseEntity<String>response=restTemplate.postForEntity(url,js.toString(),String.class);
			sendLogsRepo.save(SendLogs.builder()
					.datetime(LocalDateTime.now())
					.request(js.toString())
					.response(response.getBody())
					.status(response.getStatusCode().toString())
					.msisdn(ani)
					.build());
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}