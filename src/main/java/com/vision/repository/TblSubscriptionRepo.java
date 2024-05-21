package com.vision.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vision.entity.TableSubscription;
public interface TblSubscriptionRepo extends JpaRepository<TableSubscription, Long>{
	
	TableSubscription findByAni(String ani);

}
