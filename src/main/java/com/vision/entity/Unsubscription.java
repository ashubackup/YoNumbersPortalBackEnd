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
@Table(name = "tbl_subscription_unsub")
public class Unsubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recordstatus;
    private String status;
    private Long ani;
    private LocalDateTime billing_date;
    private String charging_date;
    private String default_amount;
    private String lang;
    private String last_billed_date;
    private String m_act;
    private String m_deact;
    private LocalDateTime next_billed_date;
    private String pack_type;
    private String product_id;
    private String provider;
    private String service_type;
    private LocalDateTime sub_date_time;
    private LocalDateTime unsub_date_time;
	

}
