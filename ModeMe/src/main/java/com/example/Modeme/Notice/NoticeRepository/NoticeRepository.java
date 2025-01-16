package com.example.Modeme.Notice.NoticeRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.Notice.Noticeentity.Notice;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // 작성 날짜 기준 내림차순 정렬
    List<Notice> findAllByOrderByCreatedDateDesc();

    // 이전 공지 (현재 ID보다 작은 값 중 가장 큰 값)
    Optional<Notice> findTop1ByIdLessThanOrderByIdDesc(Long id);

    // 다음 공지 (현재 ID보다 큰 값 중 가장 작은 값)
    Optional<Notice> findTop1ByIdGreaterThanOrderByIdAsc(Long id);

    Page<Notice> findByTitleContaining(String keyword, Pageable pageable);

    Page<Notice> findByContentContaining(String keyword, Pageable pageable);

    Page<Notice> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

    Page<Notice> findByAuthorContaining(String keyword, Pageable pageable);
}

