package com.vision.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vision.entity.TblPriceInfo;

public interface TblPriceInfoRepo extends JpaRepository<TblPriceInfo, Integer>{
	
	@Query(value="SELECT * FROM tbl_price_info WHERE pack=:pack ",nativeQuery = true)
	TblPriceInfo findPriceByPack(@Param("pack") String pack);
	
	
	@Query(value="SELECT service,price FROM tbl_price_info ",nativeQuery = true)
	List<Object[]> getAllPrice();
	
	
	


}
