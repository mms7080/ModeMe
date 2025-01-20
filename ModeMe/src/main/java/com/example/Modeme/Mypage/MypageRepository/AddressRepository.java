package com.example.Modeme.Mypage.MypageRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.Mypage.MypageEntity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{
	// 특정 이름으로 주소 찾기
    List<Address> findByName(String name);

    // 전화번호로 주소 찾기
    List<Address> findByPhone(String phone);

    // 아이디로 주소 찾기
	List<Address> findByUserid(String userid);
	
	Optional<Address> findByAddressidAndUserid(Long addressid, String userid);

	 void delete(Address address);

	List<Address> findAllByUserid(String userid);
}
