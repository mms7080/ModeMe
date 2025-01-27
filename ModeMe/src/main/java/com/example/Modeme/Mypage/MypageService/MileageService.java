package com.example.Modeme.Mypage.MypageService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Modeme.Mypage.MypageEntity.Mileage;
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
    	 System.out.println("saveMileage 호출됨");  // 로그 추가
        // 사용자의 주문 목록을 가져옵니다.
        List<Purchase> purchaseList = purrep.findByUsername(userid);
        
        for (Purchase pur : purchaseList) {
            // 해당 주문에 대해 마일리지가 이미 존재하는지 확인
            Optional<Mileage> existingMileage = milerep.findByOrdernum(pur.getId().toString());
            
           
            // 마일리지가 이미 존재하면 생성을 건너뛰고, 없다면 새로 생성
            if (!existingMileage.isPresent()) {
                Mileage mile = new Mileage();
                mile.setUserid(pur.getUsername());
                mile.setCreateAt(pur.getOrderDate());
                mile.setMileage((int) Math.ceil(pur.getTotalPrice() * 0.01));  // 올림 후 int로 변환
                
                System.out.println((int) Math.ceil(pur.getTotalPrice() * 0.01)); 
                mile.setOrdernum(pur.getId().toString());
                mile.setContent("주문 적립금");

                mile.setUsedMileage(usedMileage);

                // 마일리지 저장
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
