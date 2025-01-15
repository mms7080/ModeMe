package com.example.Modeme.QnA.QnaController;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Modeme.QnA.QnA.Comment;
import com.example.Modeme.QnA.QnA.Qna;
import com.example.Modeme.QnA.QnaService.CommentService;
import com.example.Modeme.QnA.QnaService.QnaService;
import com.example.Modeme.User.UserDTO.Headerlogin;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QnaController {

    private final QnaService qnaService;
    private final CommentService commentService;

    @Autowired
    Headerlogin keep; // 로그인 유지 재사용 Headerlogin 클래스

    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        keep.headerlogin(model, principal); // 로그인 유지
    }

    // QnA 목록 페이지 및 페이지네이션
    @GetMapping
    public String getQnaList(
        @RequestParam(defaultValue = "0") int page, // 현재 페이지 번호
        @RequestParam(defaultValue = "5") int size, // 페이지 크기
        Model model
    ) {
        Page<Qna> qnaPage = qnaService.getQnaList(page, size); // 최신글이 위로 정렬된 데이터
        model.addAttribute("qnaList", qnaPage.getContent()); // 현재 페이지 데이터
        model.addAttribute("currentPage", page); // 현재 페이지 번호
        model.addAttribute("totalPages", qnaPage.getTotalPages()); // 전체 페이지 수
        model.addAttribute("pageSize", size); // 페이지 크기
        return "/Notice/qnaHome";
    }


    // QnA 상세 페이지
    @GetMapping("/{id}")
    public String getQna(@PathVariable Long id, Model model) {
        Qna qna = qnaService.getQna(id);
        model.addAttribute("qna", qna);
        
        List<Comment> comments = commentService.getCommentsByQnaId(id); // 댓글 데이터
        model.addAttribute("comments", comments);
        
        return "/Notice/qnaView"; // /Notice 디렉토리에 있는 qnaView.html 반환
    }

    // QnA 작성 처리
    @PostMapping("/create")
    public String createQna(
        @RequestParam String title,
        @RequestParam String content,
        @RequestParam(required = false, defaultValue = "false") boolean isSecret, // 선택적으로 처리
        @RequestParam(required = false) String secretPassword, // 비밀번호 파라미터
        Principal principal
    ) {
        String username = principal.getName(); // 로그인된 사용자
        qnaService.createQna(title, content, isSecret, secretPassword, username);
        return "redirect:/qna";
    }


    // QnA 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteQna(@PathVariable Long id) {
        qnaService.deleteQna(id);
        return "redirect:/qna";
    }

    // QnA 작성 페이지
    @GetMapping("/write")
    public String getQnaWritePage() {
        return "/Notice/qnaWrite"; // /Notice 디렉토리에 있는 qnaWrite.html 반환
    }

    // 비밀글 확인 페이지
    @GetMapping("/{id}/secret")
    public String viewSecretPage(@PathVariable Long id, Model model) {
        Qna qna = qnaService.getQna(id);
        if (!qna.isSecret()) {
            return "redirect:/qna/" + id; // 비밀글이 아니면 바로 내용 페이지로
        }
        model.addAttribute("qnaId", id); // 비밀글 ID 전달
        return "Notice/secretPage"; // 비밀번호 입력 페이지
    }

    // 비밀글 비밀번호 확인 처리
    @PostMapping("/{id}/secret/verify")
    public String verifySecretPassword(
        @PathVariable Long id,
        @RequestParam String secretPassword,
        Model model
    ) {
        Qna qna = qnaService.getQna(id);
        if (qna.getSecretPassword() != null && qna.getSecretPassword().equals(secretPassword)) {
            return "redirect:/qna/" + id; // 비밀번호가 맞으면 상세 페이지로 리다이렉트
        }
        model.addAttribute("error", "비밀번호가 올바르지 않습니다."); // 정확한 에러 메시지 추가
        model.addAttribute("qnaId", id);
        return "/Notice/secretPage"; // 다시 비밀번호 입력 페이지로 이동
    }

  
    //댓글 작성
    @PostMapping("/{id}/comment")
    public String addComment(
        @PathVariable Long id,
        @RequestParam String content,
        Principal principal
    ) {
        String username = principal.getName(); // 로그인된 사용자
        commentService.addComment(id, content, username);
        return "redirect:/qna/" + id; // 댓글 추가 후 QnA 상세 페이지로 리다이렉트
    }
    //댓글 삭제
    @PostMapping("/{qnaId}/comment/{commentId}/delete")
    public String deleteComment(
        @PathVariable Long qnaId,
        @PathVariable Long commentId,
        Principal principal
    ) {
        String username = principal.getName(); // 로그인된 사용자
        commentService.deleteComment(commentId, username); // 삭제 서비스 호출
        return "redirect:/qna/" + qnaId; // QnA 상세 페이지로 리다이렉트
    }

    
    

}
