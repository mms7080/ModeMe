package com.example.Modeme.QnA.QnaService;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Modeme.QnA.QnA.Comment;
import com.example.Modeme.QnA.QnA.Qna;
import com.example.Modeme.QnA.QnARepository.CommentRepository;
import com.example.Modeme.QnA.QnARepository.QnaRepository;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final QnaRepository qnaRepository;
    private final UserRepository userRepository;

    public List<Comment> getCommentsByQnaId(Long qnaId) {
        return commentRepository.findByQnaIdOrderByCreatedAtDesc(qnaId);
    }
    //제목옆에 댓글갯수 로직
    public int getCommentCountByQnaId(Long qnaId) {
        return commentRepository.countByQnaId(qnaId);
    }
    //댓글 작성 로직
    public Comment addComment(Long qnaId, String content, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Qna qna = qnaRepository.findById(qnaId)
            .orElseThrow(() -> new RuntimeException("QnA not found"));

        Comment comment = new Comment();
        comment.setQna(qna);
        comment.setUser(user);
        comment.setContent(content);

        return commentRepository.save(comment);
    }
    //댓글 삭제 로직
    public void deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new RuntimeException("Comment not found"));

        // 권한 확인: 작성자만 삭제 가능
        if (!comment.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to delete this comment.");
        }

        commentRepository.deleteById(commentId);
    }
    //댓글 수정 로직
    public void editComment(Long commentId, String content, String username) throws AccessDeniedException {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        
        // 작성자 본인인지 확인
        if (!comment.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        // 댓글 내용 수정
        comment.setContent(content);
        commentRepository.save(comment);
    }

}