package com.example.Modeme.Mypage.MypageService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Modeme.Mypage.MypageEntity.Address;
import com.example.Modeme.Mypage.MypageEntity.Defaultaddress;
import com.example.Modeme.Mypage.MypageRepository.AddressRepository;
import com.example.Modeme.Mypage.MypageRepository.DefaultaddressRepository;

@Service
public class DefaultaddressService {

	private final DefaultaddressRepository defaultrep;

	 @Autowired
	    public DefaultaddressService(DefaultaddressRepository defaultrep, AddressRepository addressrep) {
	        this.defaultrep = defaultrep;
	    }

    // 새로운 주소를 생성
    public Defaultaddress createAddress(Defaultaddress address) {
        return defaultrep.save(address);
    }

    // 모든 주소를 조회
    public List<Defaultaddress> getAllAddresses() {
        return defaultrep.findAll();
    }

    // ID로 주소를 조회
    public Optional<Defaultaddress> getAddressById(Long id) {
        return defaultrep.findById(id);
    }

    // 기존 주소를 수정
    public Defaultaddress updateAddress(Long id, Defaultaddress updatedAddress) {
        return defaultrep.findById(id).map(address -> {
            address.setUserid(updatedAddress.getUserid());
            address.setName(updatedAddress.getName());
            address.setPhone(updatedAddress.getPhone());
            address.setAddress(updatedAddress.getAddress());
            return defaultrep.save(address);
        }).orElseThrow(() -> new IllegalArgumentException("Address with ID " + id + " not found"));
    }

 // ID로 주소를 삭제 (주어진 ID를 제외하고 나머지 주소 삭제)
    public void deleteDefaultAddress(String userid, Long addressId) {
    	// 해당 사용자의 모든 기본 주소 조회
        List<Defaultaddress> userAddresses = defaultrep.findByUserid(userid);
        
		// 주어진 addressId를 제외한 나머지 주소를 삭제
	    for (Defaultaddress address : userAddresses) {
	        if (!address.getAddressid().equals(addressId)) {
	            defaultrep.delete(address);  // 나머지 주소 삭제
	        }
	    }
    }
    
  //주소 삭제
    public void deleteDefault(String userid, Long addressid) {
    	// 해당 사용자의 주소 중에서 주어진 ID를 조회
        Optional<Defaultaddress> address = defaultrep.findByAddressidAndUserid(addressid, userid);
        
     // 조회된 주소 삭제
        defaultrep.delete(address.get());
    }
}
