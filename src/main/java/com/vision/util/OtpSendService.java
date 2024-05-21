package com.vision.util;


import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vision.entity.TblOtp;
import com.vision.entity.TblSms;
import com.vision.repository.TblOtpRepository;
import com.vision.repository.TblSmsRepo;

@Service
public class OtpSendService {
	@Autowired
	private TblOtpRepository otpRepo;
	@Autowired
	private TblSmsRepo smsSaveRepo;
//	@Autowired
//	private TblOtpSmsLogsRepo otpLogsRepo;
	
	
//	String notifyUrl="http://31.31.31.43:7474/CashChamp/smsgatewayresponse";
//	String apiUrl="http://192.168.101.218:5555/api/bulksms/sms/outbound/DigitalLoyalty/requests/";
//	String apiToken="$2a$12$.IeAehQVKlWs8f76f8P28Of143lq9AFzdYejrBTbElq2TQJLxZ78i";
//	
//	public void sendOTP(String ani, String otp)
//	{
//		String message="Hello! Your one-time password  for YoNumbers authentication is "+otp+". Please use this code within the next 3 minutes to complete your login.";
//		try
//		{
//			JSONArray array=new JSONArray();
//			array.put("263"+ani);
//			
//			JSONObject object = new JSONObject();
//			object.put("from","683");
//			object.put("to",array);
//			object.put("message",message);
//			object.put("notifyUrl",notifyUrl);
//			
//			URL url = new URL(apiUrl);
//			System.out.println(apiUrl);
//
//			HttpURLConnection http = (HttpURLConnection) url.openConnection();
//			http.setRequestMethod("POST");
//			http.setDoOutput(true);
//			http.setRequestProperty("content-type", "application/json");
//			http.setRequestProperty("application-token",apiToken);
//			
//			System.out.println("Send Json"+object);
//
//			String result="";
//			byte[] out = object.toString().getBytes(StandardCharsets.UTF_8);
//			OutputStream stream = http.getOutputStream();
//			stream.write(out);
//			System.out.println(http.getResponseCode());
//			BufferedReader br = null;
//			br = new BufferedReader(new InputStreamReader(http.getInputStream()));
//
//			Object strCurrentLine;
//			while ((strCurrentLine = br.readLine()) != null) {
//				result += strCurrentLine;
//			}
//
//			http.disconnect();
//			
//			otpLogsRepo.save(TblOtpSmsLogs.builder()
//					.ani(ani)
//					.request(object.toString())
//					.response(result)
//					.status(String.valueOf( http.getResponseCode()))
//					.build());
//			
//
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//			otpLogsRepo.save(TblOtpSmsLogs.builder()
//					.response(e.getMessage())
//					.build());
//		}
//		
//	}
	
	public void sendOTP(String ani)
	{
		try
		{
			String otp = generateAndStoreOtp(ani);
			System.out.println("Otp is-- " + otp);
			String message="Hello! Your one-time password  for YoNumbers authentication is "+otp+". Please use this code within the next 3 minutes to complete your login.";
			
			smsSaveRepo.save(TblSms.builder()
					.ani(ani)
					.message(message)
					.status("0")
					.build());		
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
	

	
	public String generateAndStoreOtp(String ani) {
		String otp ="456788";
		try {
			TblOtp otpExist = otpRepo.findByAni(ani);
			if(otpExist != null)
			{
				otpRepo.delete(otpExist);
			}
			
	        otp = generateOtp();
	        var user = TblOtp.builder()
		        .ani(ani)
		        .otp(otp)
		        .expire_At(LocalDateTime.now().plusMinutes(3))
		        .created_At(LocalDateTime.now())
		        .build();
	        otpRepo.save(user);
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return otp;
		}
		return otp;
	 }
	
	private String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }
	
	
}
