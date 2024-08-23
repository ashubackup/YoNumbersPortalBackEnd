package com.vision.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vision.entity.Unsubscription;

@Repository
public interface UnsubscriptionRepo extends JpaRepository<Unsubscription, Long>{

}
