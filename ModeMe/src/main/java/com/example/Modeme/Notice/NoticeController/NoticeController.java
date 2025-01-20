package com.example.Modeme.Notice.NoticeController;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.Modeme.Notice.NoticeService.NoticeService;
import com.example.Modeme.Notice.Noticedto.NoticeDto;
import com.example.Modeme.Notice.Noticeentity.Notice;
import com.example.Modeme.User.UserDTO.Headerlogin;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserService.UserService;

@Controller
@RequestMapping("/notices")
public class NoticeController {
    private final NoticeService noticeService;
    private final UserService userService;

    public NoticeController(NoticeService noticeService, UserService userService) {
        this.noticeService = noticeService;
        this.userService = userService;
    }

    @Autowired
    Headerlogin keep;
    //로그인 정보유지
    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        keep.headerlogin(model, principal);

        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
    }

    // 공지 목록 및 검색 기능
    @GetMapping
    public String getNoticeList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(required = false) String option,
        @RequestParam(required = false) String keyword,
        Model model
    ) {
        Page<Notice> noticePage;

        if (option != null && keyword != null && !keyword.isBlank()) {
            // 검색 호출
            noticePage = noticeService.searchNotices(option, keyword, page, size);
        } else {
            // 기본 목록 호출
            noticePage = noticeService.getNoticeList(page, size);
        }

        List<Notice> notices = noticePage.getContent();

        model.addAttribute("notices", notices);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", noticePage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "/Notice/noticeHome";
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

    // 공지 작성 화면
    @GetMapping("/new")
    public String createNoticeForm(Principal principal) {
        if (isAdmin(principal)) {
            return "/Notice/NoticeWrite";
        }
        return "redirect:/notices";
    }

    // 공지 작성 처리
    @PostMapping("/new")
    public String createNotice(@ModelAttribute NoticeDto noticeDto, Principal principal) {
        if (isAdmin(principal)) {
            noticeService.saveNotice(noticeDto);
            return "redirect:/notices";
        }
        return "redirect:/notices";
    }

    // 공지 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteNotice(@PathVariable Long id, Principal principal) {
        if (isAdmin(principal)) {
            noticeService.deleteNotice(id);
        }
        return "redirect:/notices";
    }

    // 공지 수정 화면
    @GetMapping("/edit/{id}")
    public String editNoticeForm(@PathVariable Long id, Model model, Principal principal) {
        if (isAdmin(principal)) {
            NoticeDto noticeDto = noticeService.getNoticeById(id);
            model.addAttribute("notice", noticeDto);
            return "/Notice/NoticeEdit";
        }
        return "redirect:/notices";
    }

    // 공지 수정 처리
    @PostMapping("/edit/{id}")
    public String updateNotice(@PathVariable Long id, @ModelAttribute NoticeDto noticeDto, Principal principal) {
        if (isAdmin(principal)) {
            noticeService.updateNotice(id, noticeDto);
            return "redirect:/notices/view/" + id;
        }
        return "redirect:/notices";
    }

    // 관리자 권한 확인 헬퍼 메서드
    private boolean isAdmin(Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            return "admin".equals(user.getRole());
        }
        return false;
    }
}
