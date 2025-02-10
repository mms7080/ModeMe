package com.example.Modeme.Mypage.MypageService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.Manager.ManagerRepository.AddItemRepository;
import com.example.Modeme.Mypage.MypageEntity.Wishlist;
import com.example.Modeme.Mypage.MypageRepository.WishlistRepository;

@Service
public class WishlistService {
	private final WishlistRepository wishrep;
	
	@Autowired
	public WishlistService(WishlistRepository wishrep) {
		this.wishrep = wishrep;
	}
	

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private AddItemRepository addItemRepository;

    //관심상품 삭제
    public void deleteWishlist(String userid, Long wishid) {
    	// 해당 사용자의 아이디의 관심상품만 찾기
    	Optional<Wishlist> wish = wishrep.findByWishidAndUserid(wishid, userid);
    	
    	// 조회된 관심상품 삭제
    	wishrep.delete(wish.get());
    }
    
    public boolean addToWishlist(String userId, Long itemNumber) {
        // 이미 위시리스트에 존재하는지 확인
        if (wishlistRepository.existsByUseridAndItemNumber(userId, itemNumber)) {
            return false; // 중복된 상품이면 추가하지 않음
        }

        // AddItem 테이블에서 상품 정보 가져오기
        Optional<AddItem> addItemOptional = addItemRepository.findById(itemNumber);
        if (addItemOptional.isEmpty()) {
            return false; // 상품이 존재하지 않으면 추가하지 않음
        }

        AddItem addItem = addItemOptional.get();

        // 새로운 위시리스트 객체 생성
        Wishlist wishlist = new Wishlist();
        wishlist.setUserid(userId.toString()); // userId를 문자열로 변환하여 저장
        wishlist.setItemNumber(itemNumber);
        wishlist.setItemname(addItem.getName());
        wishlist.setImage(!addItem.getImageUrls().isEmpty() ? addItem.getImageUrls().get(0) : "/image/default.jpg");
        wishlist.setPrice(addItem.getPrice());

        wishlistRepository.save(wishlist);
        return true;
    }


	


	
}
