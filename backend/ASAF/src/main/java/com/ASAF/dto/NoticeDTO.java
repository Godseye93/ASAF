package com.ASAF.dto;

import com.ASAF.entity.NoticeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoticeDTO {
    private int id;
    private String title;
    private String content;
    private long register_time;
    private long send_time;
    private int sender;
    private int receiver;
    private Boolean notification;

    public static NoticeDTO toNoticeDTO(NoticeEntity noticeEntity) {
        NoticeDTO noticeDTO = new NoticeDTO();
        noticeDTO.setId(noticeEntity.getId());
        noticeDTO.setTitle(noticeEntity.getTitle());
        noticeDTO.setContent(noticeEntity.getContent());
        noticeDTO.setRegister_time(noticeEntity.getRegister_time());
        noticeDTO.setSend_time(noticeEntity.getSend_time());
        noticeDTO.setReceiver(noticeEntity.getReceiver());
        noticeDTO.setSender(noticeEntity.getSender());
        noticeDTO.setNotification(noticeEntity.getNotification());
        return noticeDTO;
    }
}
