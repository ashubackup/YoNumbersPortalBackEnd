package com.vision.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.vision.entity.TblOtp;

public interface TblOtpRepository extends JpaRepository<TblOtp, Integer>{
	
	TblOtp findByAni(String ani);

}
