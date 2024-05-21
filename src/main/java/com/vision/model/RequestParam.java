package com.vision.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestParam {
	

    @JsonProperty("cpId")
    private String cpId;
	
	
    @JsonProperty("planId") //subscriptionOfferID
    private String subscriptionOfferID;
    
    @JsonProperty("chargeAmount")
    private String chargeAmount;
   
   
}
