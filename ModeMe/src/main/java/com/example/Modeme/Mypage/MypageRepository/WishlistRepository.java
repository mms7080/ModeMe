package com.example.Modeme.Mypage.MypageRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.Mypage.MypageEntity.Address;
import com.example.Modeme.Mypage.MypageEntity.Wishlist;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long>{
	
	List<Wishlist> findByUserid(String userid);

	Optional<Wishlist> findByWishidAndUserid(Long wishid, String userid);
	
	void delete(Wishlist wishlist);

	boolean existsByUseridAndItemNumber(String userId, Long itemNumber);

	
}
