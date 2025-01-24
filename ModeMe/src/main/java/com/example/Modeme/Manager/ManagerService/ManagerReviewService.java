package com.example.Modeme.Manager.ManagerService;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.prdDetail.entity.ProductReview;
import com.example.Modeme.prdDetail.repository.ProductReviewRepository;



@Service
public class ManagerReviewService {
	@Autowired
	private ProductReviewRepository prs;
	
	public Page<ProductReview> getReviews(Pageable pageable){
        // 내림차순 정렬: id 기준으로 내림차순 정렬
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("id")));
        return prs.findAll(pageable);
	}
	
	 public Page<ProductReview> searchReviews(String option, String keyword, Pageable pageable) {
	        if ("productName".equals(option)) {
	            return prs.findByAddItemNameContaining(keyword, pageable);
	        } else if ("user".equals(option)) {
	            return prs.findByUsersUsernameContaining(keyword, pageable);
	        } else {
	            return Page.empty(pageable);  // 잘못된 검색 옵션 처리
	        }
	    }
	
}
