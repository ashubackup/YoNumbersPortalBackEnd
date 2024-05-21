package com.vision.entity;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tbl_billinglogs")
public class TblBillingLogs 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer pid;
	private Long id;
	private String ani;
	private String total_amount;
	private String deducted_amount;
	private LocalDateTime datetime;
	private String recordstatus;
	private String errordesc;
	private String type_event;
	private LocalDateTime process_datetime;
	private String mode;
	private String servicename;
	private String subservicename;
	private String product_id;
	private String pack_type;
	private String txnId;
	private String request;
	private String response;
}