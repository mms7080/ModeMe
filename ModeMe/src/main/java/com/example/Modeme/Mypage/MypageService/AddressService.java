package com.example.Modeme.Mypage.MypageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.Modeme.Mypage.MypageEntity.Address;
import com.example.Modeme.Mypage.MypageRepository.AddressRepository;

import jakarta.transaction.Transactional;

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

	// userid로 주소들 가져오기
	public List<Address> getUserAddresses(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername(); // 로그인한 유저의 아이디 가져오기
        return addressrep.findByUserid(username);
    }

    //주소 삭제
    public void deleteAddress(String userid, Long addressid) {
    	// 해당 사용자의 주소 중에서 주어진 ID를 조회
        Optional<Address> address = addressrep.findByAddressidAndUserid(addressid, userid);
        
     // 조회된 주소 삭제
        addressrep.delete(address.get());
    }
    
    
    
    // addressid로 특정 주소 반환
    public Address getAddressById(Long addressid) {
        return addressrep.findByAddressid(addressid)
                .orElseThrow(() -> new IllegalArgumentException("주소를 찾을 수 없습니다."));
    }
    
    
    // 새로운 배송지 저장
    @Transactional
    public boolean saveAddressIfNotExists(String userId, String name, String phone, String postcode, String basicAddress, String detailAddress) {
        List<Address> userAddresses = addressrep.findByUserid(userId);

        // ✅ 기존 Address 테이블에서 저장된 우편번호 목록 가져오기
        for (Address existingAddress : userAddresses) {
            String existingFullAddress = existingAddress.getAddress();
            String existingPostcode = existingFullAddress.split(" ")[0]; // 첫 번째 공백 이전이 우편번호

            // ✅ 동일한 우편번호가 이미 있으면 저장하지 않음
            if (existingPostcode.equals(postcode)) {
                return false;
            }
        }

        // ✅ 새로운 주소 저장 (postcode + 기본주소 + 상세주소)
        String fullAddress = postcode + " " + basicAddress + " () " + detailAddress;
        Address newAddress = new Address(null, userId, name, phone, fullAddress);
        addressrep.save(newAddress);
        
        return true;
    }
}
