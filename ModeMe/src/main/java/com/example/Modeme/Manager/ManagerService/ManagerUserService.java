package com.example.Modeme.Manager.ManagerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.Modeme.Manager.ManagerDTO.UserDataDTO;
import com.example.Modeme.QnA.QnARepository.QnaRepository;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;
import com.example.Modeme.prdDetail.repository.ProductReviewRepository;

@Service
public class ManagerUserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductReviewRepository productReviewRepository;

	@Autowired
	private QnaRepository qnaRepository;

	public Page<UserDataDTO> getUserDataWithCounts(Pageable pageable) {
		Pageable sortedPageable = PageRequest.of( pageable.getPageNumber(),pageable.getPageSize(),Sort.by(Sort.Order.desc("id")) );
		Page<User> users = userRepository.findAll(sortedPageable);

		// User 엔티티를 UserDataDTO로 변환하며 QnA 및 리뷰 개수 계산
		return users.map(user -> {
			Long qnaCount = qnaRepository.countByUser(user);
			Long reviewCount = productReviewRepository.countByUsers(user);
			return new UserDataDTO(user, qnaCount, reviewCount);
		});
	}

	public Page<UserDataDTO> searchUsers(String option, String keyword, Pageable pageable) {
	    Page<User> userPage;

	    if ("username".equals(option)) {
	        userPage = userRepository.findByUsernameContaining(keyword, pageable);
	    } else if ("role".equals(option)) {
	        // role이 '관리자'일 경우 'admin'으로 변환하여 검색
	        if ("관리자".equals(keyword)) {
	            userPage = userRepository.findByRoleContaining("admin", pageable);
	        } else if ("일반".equals(keyword)) {
	            userPage = userRepository.findByRoleContaining("user", pageable);
	        } else {
	            // role이 '관리자'나 '일반'이 아닌 경우 일반 검색 처리
	            userPage = userRepository.findByRoleContaining(keyword, pageable);
	        }
	    } else {
	        return Page.empty(pageable); // 잘못된 검색 옵션 처리
	    }

	    // User 엔티티를 UserDataDTO로 변환
	    return userPage.map(user -> {
	        Long qnaCount = qnaRepository.countByUser(user);
	        Long reviewCount = productReviewRepository.countByUsers(user);
	        return new UserDataDTO(user, qnaCount, reviewCount);
	    });
	}

	

}
