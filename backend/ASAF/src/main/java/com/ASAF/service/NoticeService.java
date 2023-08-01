package com.ASAF.service;

import com.ASAF.dto.NoticeDTO;
import com.ASAF.entity.NoticeEntity;
import com.ASAF.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    public NoticeDTO createNotice(NoticeDTO noticeDTO) {
        NoticeEntity noticeEntity = NoticeEntity.toNoticeEntity(noticeDTO);
        noticeEntity = noticeRepository.save(noticeEntity);
        return NoticeDTO.toNoticeDTO(noticeEntity);
    }

    public NoticeDTO updateNotice(NoticeDTO noticeDTO) {
        NoticeEntity noticeEntity = NoticeEntity.toUpdateNoticeEntity(noticeDTO);
        noticeEntity = noticeRepository.save(noticeEntity);
        return NoticeDTO.toNoticeDTO(noticeEntity);
    }

    public List<NoticeDTO> getAllNotices() {
        return noticeRepository.findAll().stream()
                .map(NoticeDTO::toNoticeDTO)
                .collect(Collectors.toList());
    }

    public NoticeDTO getNoticeById(int id) {
        NoticeEntity noticeEntity = noticeRepository.findById(id).orElse(null);
        if (noticeEntity == null) {
            return null;
        }
        return NoticeDTO.toNoticeDTO(noticeEntity);
    }

    public void deleteNotice(int id) {
        noticeRepository.deleteById(id);
    }
}
