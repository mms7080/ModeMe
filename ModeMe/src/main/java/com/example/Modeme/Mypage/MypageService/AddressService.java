package com.example.Modeme.Mypage.MypageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Modeme.Mypage.MypageEntity.Address;
import com.example.Modeme.Mypage.MypageRepository.AddressRepository;

@Service
public class AddressService {
	private AddressRepository addressrep;
	
	@Autowired
    public AddressService(AddressRepository addressrep) {
        this.addressrep = addressrep;
    }
	
	//username으로 모든 주소 찾기
	public List<Address> getAddressByUsername(String username) {
	    List<Address> addresses = addressrep.findAllByUserid(username);
	    if (addresses.isEmpty()) {
	        // 예외 대신 빈 리스트 반환
	        return new ArrayList<>(); // 빈 리스트 반환
	    }
	    return addresses;
	}


    //주소 삭제
    public void deleteAddress(String userid, Long addressid) {
    	// 해당 사용자의 주소 중에서 주어진 ID를 조회
        Optional<Address> address = addressrep.findByAddressidAndUserid(addressid, userid);
        
     // 조회된 주소 삭제
        addressrep.delete(address.get());
    }
}
