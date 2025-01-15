package com.example.Modeme.QnA.QnaService;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.Modeme.QnA.QnA.Qna;
import com.example.Modeme.QnA.QnARepository.QnaRepository;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;
    private final UserRepository userRepository;

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



    public void deleteQna(Long id) {
        qnaRepository.deleteById(id);
    }
    
    public Page<Qna> getQnaList(int page, int size) {
        return qnaRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))); // createdAt 기준 내림차순 정렬
    }


    
    
}
