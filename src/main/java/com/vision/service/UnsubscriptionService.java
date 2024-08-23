package com.vision.service;

import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.vision.entity.SendLogs;
import com.vision.entity.TableSubscription;
import com.vision.entity.Unsubscription;
import com.vision.repository.TblSubscriptionRepo;
import com.vision.repository.UnsubscriptionRepo;
import com.vision.util.Api;

@Service
public class UnsubscriptionService {
	@Autowired
	private TblSubscriptionRepo subRepo;
	@Autowired
	private UnsubscriptionRepo unsubRepo;
	@Autowired
	private Api apiService;
	
	public void unsubscriptionProcess(String ani) {
		ani = ani.startsWith("0")?ani.substring("0".length()):ani;
		ani = ani.startsWith("263")?ani.substring("263".length()):ani;
		
		try {
			TableSubscription user = subRepo.findByAni(ani);
			if(user != null) {
				
				unsubRepo.save(Unsubscription.builder()
					.ani(Long.parseLong(ani))
					.default_amount(user.getDefault_amount())
					.last_billed_date(user.getLast_billed_date())
					.m_act(user.getM_act())
					.m_deact("WEB")
					.next_billed_date(user.getNext_billed_date())
					.pack_type(user.getPack())
					.service_type("games")
					.sub_date_time(user.getSub_date_time())
					.unsub_date_time(LocalDateTime.now())
					.build());		
				
	            subRepo.delete(user);
	            apiService.sendToOren(ani, user.getDefault_amount(), user.getPack(),"Deactivated");

			
			}else {
				
				unsubRepo.save(Unsubscription.builder()
				.ani(Long.parseLong(ani))
				.m_deact("WEB")
				.service_type("games")
				.unsub_date_time(LocalDateTime.now())
				.default_amount("0")
				.build());	
				
				apiService.sendToOren(ani, "0", "LGAMING_D","Deactivated");
			}
			
		}catch (NumberFormatException e) {
			e.getMessage();
	    } catch (Exception e) {
	    	e.getMessage();
	    }	
	}
	
}
