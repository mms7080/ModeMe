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

	// 사용자 정보와 해당 사용자가 작성한 QnA, 리뷰 개수를 가져오는 메서드
	public Page<UserDataDTO> getUserDataWithCounts(Pageable pageable) {
		// 페이지 정렬 설정 (id 기준 내림차순)
		Pageable sortedPageable = PageRequest.of( pageable.getPageNumber(),pageable.getPageSize(),Sort.by(Sort.Order.desc("id")) );
		// 사용자 정보 조회
		Page<User> users = userRepository.findAll(sortedPageable);

		// User 엔티티를 UserDataDTO로 변환하며 QnA 및 리뷰 개수 계산
		return users.map(user -> {
			// 해당 사용자가 작성한 QnA와 리뷰 개수를 각각 계산
			Long qnaCount = qnaRepository.countByUser(user);
			Long reviewCount = productReviewRepository.countByUsers(user);
			// 변환된 UserDataDTO 객체 반환
			return new UserDataDTO(user, qnaCount, reviewCount);
		});
	}

	// 사용자 검색 기능 (사용자명 또는 역할로 검색)
	public Page<UserDataDTO> searchUsers(String option, String keyword, Pageable pageable) {
	    Page<User> userPage;
	    
	    // 검색 옵션이 "username"인 경우 사용자명으로 검색
	    if ("username".equals(option)) {
	        userPage = userRepository.findByUsernameContaining(keyword, pageable);
	     // 검색 옵션이 "role"인 경우 역할로 검색
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
	    	// 해당 사용자가 작성한 QnA와 리뷰 개수를 각각 계산
	        Long qnaCount = qnaRepository.countByUser(user);
	        Long reviewCount = productReviewRepository.countByUsers(user);
	        // 변환된 UserDataDTO 객체 반환
	        return new UserDataDTO(user, qnaCount, reviewCount);
	    });
	}

	

}
