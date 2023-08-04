package com.ASAF.service;

import com.ASAF.dto.NoticeDTO;
import com.ASAF.entity.NoticeEntity;
import com.ASAF.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    public List<NoticeEntity> getNoticesBySenderAndRegisterTime(int sender, long queryDate) {
        // long 타입의 queryDate를 LocalDateTime으로 변환
        LocalDateTime dateTime = Instant.ofEpochMilli(queryDate).atZone(ZoneId.systemDefault()).toLocalDateTime();
        // startDateTime을 00:00:00로 저장
        LocalDateTime startDateTime = dateTime.withHour(0).withMinute(0).withSecond(0);
        long startDateTimeLong = startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        // endDateTime을 23:59:59로 저장
        LocalDateTime endDateTime = dateTime.withHour(23).withMinute(59).withSecond(59);
        long endDateTimeLong = endDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        // sender, startDateTimeLong, endDateTimeLong로 해당 공지를 찾는다.
        return noticeRepository.findBySenderAndRegisterTimeBetween(sender, startDateTimeLong, endDateTimeLong);
    }

    // 생성한 공지를 저장한다.
    public NoticeDTO createNotice(NoticeDTO noticeDTO) {
        NoticeEntity noticeEntity = NoticeEntity.toNoticeEntity(noticeDTO);
        noticeEntity = noticeRepository.save(noticeEntity);
        return NoticeDTO.toNoticeDTO(noticeEntity);
    }

    // 수정한 공지를 저장한다.
    public NoticeDTO updateNotice(NoticeDTO noticeDTO) {
        NoticeEntity noticeEntity = NoticeEntity.toUpdateNoticeEntity(noticeDTO);
        noticeEntity = noticeRepository.save(noticeEntity);
        return NoticeDTO.toNoticeDTO(noticeEntity);
    }

    // 공지를 조회한다.
    public List<NoticeDTO> getAllNotices() {
        return noticeRepository.findAll().stream()
                .map(NoticeDTO::toNoticeDTO)
                .collect(Collectors.toList());
    }

    // 공지 id로 공지를 조회한다.
    public NoticeDTO getNoticeById(int id) {
        NoticeEntity noticeEntity = noticeRepository.findById(id).orElse(null);
        if (noticeEntity == null) {
            return null;
        }
        return NoticeDTO.toNoticeDTO(noticeEntity);
    }

    // 공지를 삭제한다.
    public void deleteNotice(int id) {
        noticeRepository.deleteById(id);
    }
}
