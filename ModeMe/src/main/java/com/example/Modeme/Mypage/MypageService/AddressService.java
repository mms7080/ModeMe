package com.example.Modeme.Mypage.MypageService;

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

    // 모든 주소 조회
    public List<Address> getAllAddresses() {
        return addressrep.findAll();
    }

    // ID로 주소 조회
    public Address getAddressById(Long id) {
        return addressrep.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Address not found with id: " + id));
    }

    // 새로운 주소 생성
    public Address createAddress(Address address) {
        if (address.getName() == null || address.getPhone() == null || address.getAddress() == null) {
            throw new IllegalArgumentException("All fields must be provided");
        }
        return addressrep.save(address);
    }

    // 주소 수정
    public Address updateAddress(Long id, Address updatedAddress) {
        Address existingAddress = getAddressById(id);

        existingAddress.setName(updatedAddress.getName());
        existingAddress.setPhone(updatedAddress.getPhone());
        existingAddress.setAddress(updatedAddress.getAddress());

        return addressrep.save(existingAddress);
    }

    //주소 삭제
    public void deleteAddress(String userid, Long addressid) {
    	// 해당 사용자의 주소 중에서 주어진 ID를 조회
        Optional<Address> address = addressrep.findByAddressidAndUserid(addressid, userid);
        
     // 조회된 주소 삭제
        addressrep.delete(address.get());
    }
}
