package com.example.Modeme.QnA.QnARepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.QnA.QnA.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByQnaIdOrderByCreatedAtDesc(Long qnaId); // QnA ID 기준으로 댓글 조회
    void deleteByQnaId(Long qnaId); // QnA ID로 댓글 삭제
}
