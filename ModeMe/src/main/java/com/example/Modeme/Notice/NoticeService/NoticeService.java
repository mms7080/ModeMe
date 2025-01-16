package com.example.Modeme.Notice.NoticeService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.Modeme.Notice.NoticeRepository.NoticeRepository;
import com.example.Modeme.Notice.Noticedto.NoticeDto;
import com.example.Modeme.Notice.Noticeentity.Notice;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    // 공지 저장
    public Notice saveNotice(NoticeDto noticeDto) {
        Notice notice = new Notice();
        notice.setTitle(noticeDto.getTitle());
        notice.setContent(noticeDto.getContent());
        notice.setAuthor("관리자");
        return noticeRepository.save(notice);
    }

    // 최신글이 위로 오게 정렬된 페이지네이션
    public Page<Notice> getNotices(int page, int size) {
        return noticeRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")));
    }

    // 모든 공지 조회 (내림차순)
    public List<NoticeDto> getAllNotices() {
        return noticeRepository.findAllByOrderByCreatedDateDesc()
            .stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    // 공지 삭제
    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }

    // 공지 상세 조회
    public NoticeDto getNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다. ID: " + id));
        return convertToDto(notice);
    }

    // 공지 수정
    public void updateNotice(Long id, NoticeDto noticeDto) {
        Notice notice = noticeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다. ID: " + id));
        notice.setTitle(noticeDto.getTitle());
        notice.setContent(noticeDto.getContent());
        noticeRepository.save(notice);
    }

    // 이전 공지
    public Optional<NoticeDto> getPreviousNotice(Long id) {
        return noticeRepository.findTop1ByIdLessThanOrderByIdDesc(id)
            .map(this::convertToDto);
    }

    // 다음 공지
    public Optional<NoticeDto> getNextNotice(Long id) {
        return noticeRepository.findTop1ByIdGreaterThanOrderByIdAsc(id)
            .map(this::convertToDto);
    }

    // DTO 변환
    private NoticeDto convertToDto(Notice notice) {
        NoticeDto dto = new NoticeDto();
        dto.setId(notice.getId());
        dto.setTitle(notice.getTitle());
        dto.setContent(notice.getContent());
        dto.setAuthor(notice.getAuthor());
        return dto;
    }
    //공지 검색리스트
    public Page<Notice> getNoticeList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        return noticeRepository.findAll(pageable);
    }

    
    // 공지 검색
    public Page<Notice> searchNotices(String option, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        switch (option) {
            case "title":
                return noticeRepository.findByTitleContaining(keyword, pageable);
            case "content":
                return noticeRepository.findByContentContaining(keyword, pageable);
            case "title_content":
                return noticeRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
            case "author":
                return noticeRepository.findByAuthorContaining(keyword, pageable);
            default:
                return Page.empty();
        }
    }
}
