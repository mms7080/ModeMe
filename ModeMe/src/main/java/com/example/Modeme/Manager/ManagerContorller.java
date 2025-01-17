package com.example.Modeme.Manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.Modeme.QnA.QnARepository.QnaRepository;
import com.example.Modeme.User.UserRepository.UserRepository;

@Controller
public class ManagerContorller {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QnaRepository qnaRepository;
    
    
    //관리자 메인
    @GetMapping("/managerMain")
    public String adminDashboard(Model model) {
        // 총 회원 수
        long totalUsers = userRepository.count();

        // QnA 글 수
        long totalQnAs = qnaRepository.count();

        // 모델에 데이터 추가
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalQnAs", totalQnAs);

        return "manager/managerMain"; // 
    }
}

