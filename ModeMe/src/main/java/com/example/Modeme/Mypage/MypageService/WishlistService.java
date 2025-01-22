package com.example.Modeme.Mypage.MypageService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Modeme.Mypage.MypageEntity.Wishlist;
import com.example.Modeme.Mypage.MypageRepository.WishlistRepository;

@Service
public class WishlistService {
	private final WishlistRepository wishrep;
	
	@Autowired
	public WishlistService(WishlistRepository wishrep) {
		this.wishrep = wishrep;
	}

	
	//관심상품 삭제
    public void deleteWishlist(String userid, Long wishid) {
    	// 해당 사용자의 아이디의 관심상품만 찾기
    	Optional<Wishlist> wish = wishrep.findByWishidAndUserid(wishid, userid);
        
     // 조회된 관심상품 삭제
        wishrep.delete(wish.get());
    }
}
