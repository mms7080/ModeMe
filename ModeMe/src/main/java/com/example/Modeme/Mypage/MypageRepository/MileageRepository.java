package com.example.Modeme.Mypage.MypageRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.Mypage.MypageEntity.Mileage;

@Repository
public interface MileageRepository extends JpaRepository<Mileage, Long>{
	List<Mileage> findByUserid(String userid);

	Optional<Mileage> findByOrdernum(String string);
}
