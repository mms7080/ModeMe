package com.example.Modeme.Mypage.MypageRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Modeme.Mypage.MypageEntity.Mileage;
import com.example.Modeme.purchase.dto.Purchase;

import jakarta.transaction.Transactional;

@Repository
public interface MileageRepository extends JpaRepository<Mileage, Long>{
	List<Mileage> findByUserid(String userid);

	List<Mileage> findByOrdernum(String string);
	
}
