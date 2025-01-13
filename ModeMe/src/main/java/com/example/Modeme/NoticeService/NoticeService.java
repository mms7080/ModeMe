package com.example.Modeme.NoticeService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.Modeme.NoticeRepsitory.NoticeRepository;
import com.example.Modeme.Noticedto.NoticeDto;
import com.example.Modeme.Noticeentity.Notice;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    // 공지 저장 메서드
    public Notice saveNotice(NoticeDto noticeDto) {
        Notice notice = new Notice();
        notice.setTitle(noticeDto.getTitle());
        notice.setContent(noticeDto.getContent());
        notice.setAuthor("관리자"); // 임시 작성자
        return noticeRepository.save(notice);
    }

    // 작성 날짜 기준 내림차순으로 공지 반환
    public List<NoticeDto> getAllNotices() {
        return noticeRepository.findAllByOrderByCreatedDateDesc()
            .stream()
            .map(notice -> {
                NoticeDto dto = new NoticeDto();
                dto.setId(notice.getId());
                dto.setTitle(notice.getTitle());
                dto.setContent(notice.getContent());
                dto.setAuthor(notice.getAuthor());
                return dto;
            })
            .collect(Collectors.toList());
    }

    // 공지 삭제 메서드
    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }

    // 특정 공지 조회 메서드
    public NoticeDto getNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다. ID: " + id));

        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setId(notice.getId());
        noticeDto.setTitle(notice.getTitle());
        noticeDto.setContent(notice.getContent());
        noticeDto.setAuthor(notice.getAuthor());
        return noticeDto;
    }
    

    // 이전 공지 가져오기
    public Optional<NoticeDto> getPreviousNotice(Long id) {
        return noticeRepository.findTop1ByIdLessThanOrderByIdDesc(id)
            .map(this::convertToDto);
    }

    // 다음 공지 가져오기
    public Optional<NoticeDto> getNextNotice(Long id) {
        return noticeRepository.findTop1ByIdGreaterThanOrderByIdAsc(id)
            .map(this::convertToDto);
    }

    // 엔티티를 DTO로 변환하는 메서드
    private NoticeDto convertToDto(Notice notice) {
        NoticeDto dto = new NoticeDto();
        dto.setId(notice.getId());
        dto.setTitle(notice.getTitle());
        dto.setContent(notice.getContent());
        dto.setAuthor(notice.getAuthor());
        return dto;
    }
    // 공지 수정
    public void updateNotice(Long id, NoticeDto noticeDto) {
        // 공지사항 조회
        Notice notice = noticeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다. ID: " + id));

        // 공지사항 데이터 수정
        notice.setId(noticeDto.getId());
        notice.setTitle(noticeDto.getTitle());
        notice.setContent(noticeDto.getContent());

        // 수정된 데이터 저장
        noticeRepository.save(notice);
    }

}
