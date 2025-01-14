package com.example.Modeme.Notice.NoticeController;

import java.security.Principal;

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

import com.example.Modeme.Notice.NoticeService.NoticeService;
import com.example.Modeme.Notice.Noticedto.NoticeDto;
import com.example.Modeme.Notice.Noticeentity.Notice;
import com.example.Modeme.User.UserDTO.Headerlogin;

@Controller
@RequestMapping("/notices")
public class NoticeController {
    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }
    
	@Autowired
	Headerlogin keep; // 로그인 유지 재사용 Headerlogin 클래스
	
    @ModelAttribute //모든 매핑에 추가할 코드
    public void addAttributes(Model model, Principal principal) {
        keep.headerlogin(model, principal); //로그인 유지 
    }

    // 공지 목록 및 페이지네이션
    @GetMapping
    public String getNotices(
        @RequestParam(defaultValue = "0") int page, // 현재 페이지 번호
        @RequestParam(defaultValue = "5") int size, // 페이지 크기
        Model model
    ) {
        Page<Notice> noticePage = noticeService.getNotices(page, size); // 페이지네이션된 데이터 조회
        model.addAttribute("notices", noticePage.getContent()); // 현재 페이지 데이터
        model.addAttribute("currentPage", page); // 현재 페이지 번호
        model.addAttribute("totalPages", noticePage.getTotalPages()); // 전체 페이지 수
        model.addAttribute("pageSize", size); // 페이지 크기
        return "/Notice/NoticeHome";
    }


    // 공지 작성 화면
    @GetMapping("/new")
    public String createNoticeForm() {
        return "/Notice/NoticeWrite";
    }

    // 공지 작성 처리
    @PostMapping("/new")
    public String createNotice(@ModelAttribute NoticeDto noticeDto) {
        noticeService.saveNotice(noticeDto);
        return "redirect:/notices";
    }

    // 공지 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return "redirect:/notices";
    }

    // 공지 상세보기
    @GetMapping("/view/{id}")
    public String getNoticeDetail(@PathVariable Long id, Model model) {
        NoticeDto noticeDto = noticeService.getNoticeById(id);
        model.addAttribute("notice", noticeDto);

        // 이전글 및 다음글 추가
        noticeService.getPreviousNotice(id).ifPresent(prev -> model.addAttribute("previousNotice", prev));
        noticeService.getNextNotice(id).ifPresent(next -> model.addAttribute("nextNotice", next));

        return "/Notice/NoticeView";
    }

    // 공지 수정 화면
    @GetMapping("/edit/{id}")
    public String editNoticeForm(@PathVariable Long id, Model model) {
        NoticeDto noticeDto = noticeService.getNoticeById(id);
        model.addAttribute("notice", noticeDto);
        return "/Notice/NoticeEdit";
    }

    // 공지 수정 처리
    @PostMapping("/edit/{id}")
    public String updateNotice(@PathVariable Long id, @ModelAttribute NoticeDto noticeDto) {
        noticeService.updateNotice(id, noticeDto);
        return "redirect:/notices/view/" + id;
    }
}


    
   
