package com.example.Modeme.QnA.QnARepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.QnA.QnA.Qna;
import com.example.Modeme.User.UserEntity.User;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Long> {
    // 사용자별 QnA 조회
    List<Qna> findByUser(User user);
    
    // 사용자별 QnA 수 조회
    Long countByUser(User user); 

    // 이전 공지 (현재 ID보다 작은 값 중 가장 큰 값)
    Optional<Qna> findTop1ByIdLessThanOrderByIdDesc(Long id);

    // 다음 공지 (현재 ID보다 큰 값 중 가장 작은 값)
    Optional<Qna> findTop1ByIdGreaterThanOrderByIdAsc(Long id);

    // 페이징 처리를 위한 메서드
    Page<Qna> findAll(Pageable pageable);

    // 검색 기능 추가
    Page<Qna> findByTitleContaining(String title, Pageable pageable); // 제목 검색
    Page<Qna> findByContentContaining(String content, Pageable pageable); // 내용 검색
    Page<Qna> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable); // 제목 + 내용 검색
    Page<Qna> findByUser_UsernameContaining(String username, Pageable pageable); // 작성자 아이디 검색
}
