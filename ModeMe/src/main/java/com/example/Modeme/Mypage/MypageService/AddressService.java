package com.example.Modeme.Mypage.MypageService;

import java.util.List;

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
        if (address.getName() == null || address.getPhone() == 0 || address.getAddress() == null) {
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

    // 주소 삭제
    public void deleteAddress(Long id) {
        if (!addressrep.existsById(id)) {
            throw new IllegalArgumentException("Address not found with id: " + id);
        }
        addressrep.deleteById(id);
    }
}
