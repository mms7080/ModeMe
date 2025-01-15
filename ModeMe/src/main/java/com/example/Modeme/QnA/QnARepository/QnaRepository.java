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
    List<Qna> findByUser(User user);
    // 이전 공지 (현재 ID보다 작은 값 중 가장 큰 값)
    Optional<Qna> findTop1ByIdLessThanOrderByIdDesc(Long id);

    // 다음 공지 (현재 ID보다 큰 값 중 가장 작은 값)
    Optional<Qna> findTop1ByIdGreaterThanOrderByIdAsc(Long id);
    // 페이징 처리를 위한 메서드
    Page<Qna> findAll(Pageable pageable); 
}
