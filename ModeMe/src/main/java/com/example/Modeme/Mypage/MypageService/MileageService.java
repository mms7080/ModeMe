package com.example.Modeme.Mypage.MypageService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Modeme.Mypage.MypageEntity.Mileage;
import com.example.Modeme.Mypage.MypageEntity.Wishlist;
import com.example.Modeme.Mypage.MypageRepository.MileageRepository;
import com.example.Modeme.purchase.dao.PurchaseRepository;
import com.example.Modeme.purchase.dto.Purchase;

@Service
public class MileageService {
	
	@Autowired
    private PurchaseRepository purrep;

	private final MileageRepository milerep;

    public MileageService(MileageRepository milerep) {
        this.milerep = milerep;
    }
	
    // 결제와 동시에 마일리지 테이블에 입력
    public void saveMileage(String userid, int usedMileage) {
        // 사용자의 주문 목록을 가져옵니다.
        List<Purchase> purchaseList = purrep.findByUsername(userid);
      
        
        for (Purchase pur : purchaseList) {
            // 마일리지가 이미 존재하면 생성을 건너뛰고, 없다면 새로 생성
        	List<Mileage> existingMileages = milerep.findByOrdernum(pur.getId().toString());

        	// 마일리지가 없는 경우에만 저장
        	 if (existingMileages.isEmpty()) {
        	    Mileage mile = new Mileage();
        	    mile.setUserid(pur.getUsername());
        	    mile.setCreateAt(LocalDateTime.now());
        	    mile.setMileage((int) Math.ceil(pur.getTotalPrice() * 0.01));  
        	    mile.setOrdernum(pur.getId().toString());
        	    mile.setUsedMileage(usedMileage);
        	    mile.setContent("주문 적립금");
        	  
        	    milerep.save(mile);
        	    
        	} 

        }
    }

    // 총 적립금
    public int getTotalMileage(String userid) {
        // 사용자의 모든 마일리지 목록을 가져옵니다.
        List<Mileage> mileageList = milerep.findByUserid(userid);

        // 만약 마일리지가 없다면 0을 반환
        if (mileageList.isEmpty()) {
            return 0;
        }

        // 모든 마일리지 항목을 합산합니다.
        return mileageList.stream()
                .mapToInt(Mileage::getMileage)  // 각 마일리지 항목을 더함
                .sum();
    }

    
}
