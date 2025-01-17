package com.example.Modeme.Mypage.MypageRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Modeme.Mypage.MypageEntity.Defaultaddress;

public interface DefaultaddressRepository extends JpaRepository<Defaultaddress, Long>{
	// 특정 이름으로 주소 찾기
    List<Defaultaddress> findByName(String name);

    // 전화번호로 주소 찾기
    List<Defaultaddress> findByPhone(String phone);

    // 아이디로 주소 찾기
	List<Defaultaddress> findByUserid(String userid);

	Optional<Defaultaddress> findByAddressidAndUserid(Long addressid, String userid);

}
