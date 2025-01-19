package com.example.Modeme.QnA.QnaController;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

 // QnA 목록 및 검색 기능 추가
    @GetMapping
    public String getQnaList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(required = false) String option, // 검색 옵션
        @RequestParam(required = false) String keyword, // 검색 키워드
        Model model
    ) {
        Page<Qna> qnaPage;

        // 검색 조건이 있을 경우 검색 수행
        if (option != null && keyword != null) {
            qnaPage = qnaService.searchQna(option, keyword, page, size);
        } else {
            // 검색 조건이 없으면 기본 목록 조회
            qnaPage = qnaService.getQnaList(page, size);
        }

        List<Qna> qnaList = qnaPage.getContent();

        // 댓글 수 계산
        Map<Long, Integer> commentCounts = new HashMap<>();
        for (Qna qna : qnaList) {
            int count = commentService.getCommentCountByQnaId(qna.getId());
            commentCounts.put(qna.getId(), count);
        }

        model.addAttribute("qnaList", qnaList);
        model.addAttribute("commentCounts", commentCounts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", qnaPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("totalSize", qnaPage.getTotalElements());

        return "/Notice/qnaHome";
    }




    //QnA 상세페이지
    @GetMapping("/{id}")
    public String getQna(@PathVariable Long id, Model model) {
        Qna qna = qnaService.getQna(id);
        model.addAttribute("qna", qna);
        
        // 댓글 리스트 추가
        List<Comment> comments = commentService.getCommentsByQnaId(id);
        model.addAttribute("comments", comments);

        // 댓글 수 추가
        Map<Long, Integer> commentCounts = new HashMap<>();
        commentCounts.put(id, comments.size()); // 현재 QnA의 댓글 수
        model.addAttribute("commentCounts", commentCounts);

        return "/Notice/qnaView"; // qnaView.html로 이동
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
    public String deleteQna(@PathVariable Long id,String username) {
        qnaService.deleteQna(id, username);
        return "redirect:/qna";
    }

    // QnA 작성 페이지
    @GetMapping("/write")
    public String getQnaWritePage() {
        return "/Notice/qnaWrite"; // /Notice 디렉토리에 있는 qnaWrite.html 반환
    }

 // 비밀글 확인 페이지
    @GetMapping("/{id}/secret")
    public String viewSecretPage(@PathVariable Long id, Model model, Principal principal) {
        Qna qna = qnaService.getQna(id);

        // 비밀글이 아니면 바로 상세 페이지로 이동
        if (!qna.isSecret()) {
            return "redirect:/qna/" + id;
        }

        // 관리자 여부 확인
        boolean isAdmin = principal != null && qnaService.isAdmin(principal.getName());
        System.out.println("Principal Name: " + (principal != null ? principal.getName() : "null"));
        System.out.println("Is Admin: " + isAdmin);

        if (isAdmin) {
            // 관리자는 비밀번호 확인 없이 바로 상세 페이지로 이동
            return "redirect:/qna/" + id;
        }

        model.addAttribute("qnaId", id); // 비밀글 ID 전달
        model.addAttribute("isAdmin", isAdmin); // 관리자 여부 전달
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
    
    //QnA 삭제
    @PostMapping("/{id}/delete")
    public String deleteQna(
        @PathVariable Long id,
        Principal principal
    ) {
        String username = principal.getName(); // 현재 로그인된 사용자 이름
        qnaService.deleteQna(id, username); // 삭제 처리 서비스 호출
        return "redirect:/qna"; // 삭제 후 목록 페이지로 리다이렉트
    }

    //QnA 수정
    @GetMapping("/edit/{id}")
    public String editQna(@PathVariable Long id, Model model) {
        Qna qna = qnaService.findById(id);
        model.addAttribute("qna", qna);
        return "/Notice/qnaEdit";
    }

    //QnA 수정
    @PostMapping("/{id}/update")
    public String updateQna(@PathVariable Long id, @ModelAttribute Qna updatedQna, Principal principal) throws AccessDeniedException {
        Qna existingQna = qnaService.findById(id);

        if (!existingQna.getUser().getUsername().equals(principal.getName())) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        existingQna.setTitle(updatedQna.getTitle());
        existingQna.setContent(updatedQna.getContent());
        qnaService.save(existingQna);

        return "redirect:/qna/" + id;
    }
    //댓글 수정
    @PostMapping("/{qnaId}/comment/{commentId}/edit")
    public String editComment(
        @PathVariable Long qnaId,
        @PathVariable Long commentId,
        @RequestParam String content,
        Principal principal
    ) throws AccessDeniedException {
        String username = principal.getName();
        commentService.editComment(commentId, content, username);
        return "redirect:/qna/" + qnaId; // 수정 후 Q&A 상세 페이지로 리다이렉트
    }

    

}
