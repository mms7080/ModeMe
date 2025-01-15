package com.example.Modeme.QnA.QnaService;

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

}