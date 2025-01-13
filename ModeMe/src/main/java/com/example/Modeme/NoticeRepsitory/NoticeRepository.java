package com.example.Modeme.NoticeRepsitory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.Noticeentity.Notice;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
	// 작성 날짜 기준 내림차순 정렬
	List<Notice> findAllByOrderByCreatedDateDesc(); 
    // 이전 공지 (현재 ID보다 작은 값 중 가장 큰 값)
    Optional<Notice> findTop1ByIdLessThanOrderByIdDesc(Long id);

    // 다음 공지 (현재 ID보다 큰 값 중 가장 작은 값)
    Optional<Notice> findTop1ByIdGreaterThanOrderByIdAsc(Long id);
    // 페이징 처리를 위한 메서드
    Page<Notice> findAll(Pageable pageable); 
}
