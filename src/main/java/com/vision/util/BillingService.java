package com.vision.util;

import java.time.LocalDateTime;
import java.util.Random;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.vision.entity.TableSubscription;
import com.vision.entity.TblBillingLogs;
import com.vision.repository.TblBillingLogsRepo;
import com.vision.repository.TblSubscriptionRepo;



@Service
public class BillingService {
	
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private TblBillingLogsRepo billingLogsRepo;
	
	@Autowired
	private TblSubscriptionRepo subRepo;

	public String hitBilling(String ani,String packCode,String amount)
	{
		String resp="2";
		try
		{

			String cpId="334";
			String channelID="4";
			String username="lgaming";
			String password="gaming7321";
			String shortCode="55223";
			String uuid=String.valueOf(new Random().nextInt());
			
			String xmlData = "msisdn=263" + ani + "|productCode=" + packCode + "|channelID=" + channelID
					+ "|chargeAmount=" + amount + "|clientTransId=" + uuid + "|cpID=" + cpId + "|username=" + username
					+ "|password=" + password + "|language=0|shortCode=" + shortCode;

			String requestType = "ACTIVATE";

			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://ws.apache.org/axis2/com/sixdee/imp/axis2/dto/Request/xsd/\" xmlns:xsd1=\"http://dto.axis2.imp.sixdee.com/xsd\">"
					+ "<soap:Body>" + "<xsd:ServiceExecutor>" + "<xsd:request>" + "<xsd1:billingText>" + xmlData
					+ "</xsd1:billingText>" + "<xsd1:operationCode>" + requestType + "</xsd1:operationCode>"
					+ "</xsd:request>" + "</xsd:ServiceExecutor>" + "</soap:Body>" + "</soap:Envelope>";
			
			resp=hitForCharging(xml, String.valueOf(ani), amount, packCode);

		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resp;
	
	}
	public String hitForCharging(String xml,String ani,String amount,String packcode)
	{
		String response = "2";
		try
		{
			String url="http://172.27.100.12:9080/BL/services/SDP?wsdl";
//			SDP_TEST_URL=http://172.27.100.12:9080/BL/services/SDP?wsdl

			HttpHeaders headers=new HttpHeaders();
			headers.set("Content-Type","text/xml");
			headers.set("Accept","*/*");
			headers.set("User-Agent","Java/1.6.0_21");
			
			HttpEntity<String> entity=new HttpEntity<>(xml,headers);
			
			System.out.println("\nURL is "+url);
			System.out.println("\nRequest is "+xml);
			
			ResponseEntity<String> response1 = restTemplate.exchange(url,HttpMethod.POST,entity,String.class);
			
			System.out.println("\nResponse is "+response1.getBody());
			
			response = processResponse(response1.getBody().toString(), ani, amount, packcode, xml);
			 
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}
	
	public String processResponse(String response,String ani,String amount,String pack,String request)
	{
		String recordStatus="2";
		TblBillingLogs billing = new TblBillingLogs();
		String txnID = java.util.concurrent.ThreadLocalRandom.current().nextLong(1000000000000000L, 10000000000000000L) + "";
		try
		{
			JSONObject xmlToJson = XML.toJSONObject(response);
			Object nodeData = getNodeData("soapenv:Envelope", xmlToJson.toString());
			Object nodeData2 = getNodeData("soapenv:Body", nodeData.toString());
			Object nodeData3 = getNodeData("ns:ServiceExecutorResponse", nodeData2.toString());
			Object nodeData4 = getNodeData("ns:return", nodeData3.toString());
			Object statusCode = getNodeData("ax21:statusCode", nodeData4.toString());
			Object statusDesc = getNodeData("ax21:status", nodeData4.toString());
			
			
			String errorDesc="";
			
			if (statusCode.toString().equalsIgnoreCase("6")) 
			{
				//Charging Fail
				recordStatus="99";
				errorDesc="115";
				
				 billing.setAni(ani);
		           billing.setDatetime(LocalDateTime.now());
		           billing.setDeducted_amount(amount);
		           billing.setErrordesc("0");
		           billing.setMode("WAP");
		           billing.setPack_type("");
		           billing.setProduct_id("0");
		           billing.setRecordstatus(recordStatus);
		           billing.setServicename("games");
		           billing.setSubservicename("games");
		           billing.setTxnId(txnID);
		           billing.setType_event("SUB");
		           billingLogsRepo.save(billing);
//				sub.setBilling_date(LocalDateTime.now());
//				subRepo.save(sub);				
			}
			else if (statusCode.toString().equalsIgnoreCase("116")) 
			{
				//Charging Success
				recordStatus="1";
				errorDesc="ONLINE CHARGING SUCCESS";
				
				
				Integer days=1;
				if(pack.equalsIgnoreCase("BIGCASH_D"))
				{
					days=1;
				}
				else if(pack.equalsIgnoreCase("BIGCASH_W"))
				{
					days=7;
				}
				else if(pack.equalsIgnoreCase("BIGCASH_M"))
				{
					days=30;
				}
				
				
			   billing.setAni(ani);
	           billing.setDatetime(LocalDateTime.now());
	           billing.setDeducted_amount(amount);
	           billing.setErrordesc("0");
	           billing.setMode("WAP");
	           billing.setPack_type("");
	           billing.setProduct_id("0");
	           billing.setRecordstatus(recordStatus);
	           billing.setServicename("games");
	           billing.setSubservicename("games");
	           billing.setTxnId(txnID);
	           billing.setType_event("SUB");
	           billingLogsRepo.save(billing);
				
				//Update in tbl_subscription
				TableSubscription sub = subRepo.findByAni(ani);
				sub.setLast_billed_date(LocalDateTime.now().toString());
				sub.setNext_billed_date(LocalDateTime.now().plusDays(days));
				sub.setBilling_date(LocalDateTime.now());
				subRepo.save(sub);

				System.out.println("Data Updated in tbl_subscription");
				
				//Save in tbl_billing_success
				
				System.out.println("Data Saved in tbl_billing_success");
				
			}
			else
			{
				recordStatus="988";
				errorDesc="116";
				
//				sub.setBilling_date(LocalDateTime.now());
//				subRepo.save(sub);				

			}
						
			
			
			System.out.println("Saved in tbl_billinglogs");

			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return recordStatus;
	}
	
	public Object getNodeData(String key, String data) 
	{
		try
		{
			return new JSONObject(data).get(key);
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
