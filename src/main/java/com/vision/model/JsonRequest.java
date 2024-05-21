package com.vision.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonRequest 
{

	@JsonProperty("requestId")
    private String requestId;

    @JsonProperty("requestTimeStamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String requestTimeStamp;

    @JsonProperty("channel")
    private String channel;

    @JsonProperty("sourceNode")
    private String sourceNode;

    @JsonProperty("sourceAddress")
    private String sourceAddress;

    @JsonProperty("featureId")
    private String featureId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("externalServiceId")
    private String externalServiceId;

    @JsonProperty("requestParam")
    private RequestParam requestParam;
    
}
