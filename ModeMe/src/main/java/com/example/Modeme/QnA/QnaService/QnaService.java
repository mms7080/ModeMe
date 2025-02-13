package com.example.Modeme.QnA.QnaService;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.Modeme.QnA.QnA.Qna;
import com.example.Modeme.QnA.QnARepository.CommentRepository;
import com.example.Modeme.QnA.QnARepository.QnaRepository;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;


    public List<Qna> getQnaList() {
        return qnaRepository.findAll();
    }

    public Qna getQna(Long id) {
        return qnaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("QnA not found"));
    }

    public Qna createQna(String title, String content, boolean isSecret, String secretPassword, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Qna qna = new Qna();
        qna.setTitle(title);
        qna.setContent(content);
        qna.setSecret(isSecret);
        qna.setSecretPassword(isSecret ? secretPassword : null); // 비밀글이 아니면 비밀번호 null
        qna.setUser(user);
        qna.setCreatedAt(LocalDateTime.now());

        return qnaRepository.save(qna);
    }

    // QnA 게시글 삭제
    @Transactional
    public void deleteQna(Long id, String username) {
        Qna qna = qnaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 로그인된 사용자가 작성자인지 확인
        if (!qna.getUser().getUsername().equals(username)) {
            throw new RuntimeException("게시글 삭제 권한이 없습니다.");
        }
        // 먼저 관련된 댓글 삭제
        commentRepository.deleteByQnaId(id);
        // QnA 삭제
        qnaRepository.deleteById(id);
    }

    public Page<Qna> getQnaList(int page, int size) {
        return qnaRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))); // createdAt 기준 내림차순 정렬
    }

    // QnA 수정
    public Qna findById(Long id) {
        return qnaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Q&A not found with id: " + id));
    }

    public void save(Qna qna) {
        qnaRepository.save(qna);
    }
 // 관리자 여부 확인
    public boolean isAdmin(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // 역할(Role)이 "admin" 또는 "ROLE_ADMIN"인 경우 관리자로 판단
        String role = user.getRole();
        System.out.println("Database Role: " + role); // 역할 값 출력
        return "ROLE_ADMIN".equalsIgnoreCase(role.trim()) || "admin".equalsIgnoreCase(role.trim());
    }




    // ======== 검색 기능 추가 ========
    public Page<Qna> searchQna(String option, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        switch (option) {
            case "title":
                return qnaRepository.findByTitleContaining(keyword, pageable);
            case "content":
                return qnaRepository.findByContentContaining(keyword, pageable);
            case "title_content":
                return qnaRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
            case "id":
                return qnaRepository.findByUser_UsernameContaining(keyword, pageable);
            default:
                throw new IllegalArgumentException("Invalid search option: " + option);
        }
    }
}
