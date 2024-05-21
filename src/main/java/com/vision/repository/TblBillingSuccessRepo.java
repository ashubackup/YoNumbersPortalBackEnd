package com.vision.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vision.entity.TblBillingSuccess;



@Repository
public interface TblBillingSuccessRepo extends JpaRepository<TblBillingSuccess,Long>
{

}