package com.vision.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vision.entity.TblSms;

@Repository
public interface TblSmsRepo extends JpaRepository<TblSms, Integer>{

}
