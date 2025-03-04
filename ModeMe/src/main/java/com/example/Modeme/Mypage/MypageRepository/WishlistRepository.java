package com.example.Modeme.Mypage.MypageRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.Mypage.MypageEntity.Address;
import com.example.Modeme.Mypage.MypageEntity.Wishlist;
import com.example.Modeme.purchase.dto.Purchase;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long>{
	
	List<Wishlist> findByUserid(String userid);

	Optional<Wishlist> findByWishidAndUserid(Long wishid, String userid);
	
	void delete(Wishlist wishlist);

	boolean existsByUseridAndItemNumber(String userId, Long itemNumber);

	Page<Wishlist> findByUserid(String username, Pageable pageable);
}
