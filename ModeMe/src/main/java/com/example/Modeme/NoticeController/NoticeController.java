package com.example.Modeme.NoticeController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Modeme.NoticeService.NoticeService;
import com.example.Modeme.Noticedto.NoticeDto;

@Controller 
@RequestMapping("/notices") // "/notices" 경로로 시작하는 요청을 처리
public class NoticeController {
    private final NoticeService noticeService;

    // NoticeService를 생성자 주입 방식으로 주입
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    // 공지 목록
    @GetMapping
    public String getAllNotices(Model model) {
        model.addAttribute("notices", noticeService.getAllNotices());
        return "/Notice/NoticeHome";
    }

    // 공지 작성
    @GetMapping("/new")
    public String createNoticeForm() {
        return "/Notice/NoticeWrite";
    }

    // 공지 작성 요청
    @PostMapping("/new")
    public String createNotice(@ModelAttribute NoticeDto noticeDto) {
        noticeService.saveNotice(noticeDto);
        return "redirect:/notices";
    }

    // 공지 삭제 요청
    @PostMapping("/delete/{id}")
    public String deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return "redirect:/notices";
    }
    
    // 특정 공지의 상세 정보 
    @GetMapping("/view/{id}")
    public String getNoticeDetail(@PathVariable Long id, Model model) {
        NoticeDto noticeDto = noticeService.getNoticeById(id);
        model.addAttribute("notice", noticeDto);

        // 이전글 및 다음글을 모델에 추가
        noticeService.getPreviousNotice(id).ifPresent(prev -> model.addAttribute("previousNotice", prev));
        noticeService.getNextNotice(id).ifPresent(next -> model.addAttribute("nextNotice", next));

        return "/Notice/NoticeView";
    }
    
    //공지 수정 메핑
    @GetMapping("/edit/{id}")
    public String editNoticeForm(@PathVariable Long id, Model model) {
        NoticeDto noticeDto = noticeService.getNoticeById(id); // ID로 공지 데이터 조회
        model.addAttribute("notice", noticeDto); // 모델에 데이터 추가
        return "/Notice/NoticeEdit"; // 수정 화면 템플릿 반환
    }
    //공지 수정 처리
    @PostMapping("/edit/{id}")
    public String updateNotice(@PathVariable Long id, @ModelAttribute NoticeDto noticeDto) {
        noticeService.updateNotice(id, noticeDto); // 공지사항 수정 로직 호출
        return "redirect:/notices/view/" + id; // 수정 후 상세 페이지로 리다이렉트
    }


    
    
}
