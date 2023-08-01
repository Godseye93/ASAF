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
    private String Title;
    private String content;
    private Date register_time;
    private Date send_time;
    private String writter;
    private int sender;
    private Boolean notification;

    public static NoticeDTO toNoticeDTO(NoticeEntity noticeEntity) {
        NoticeDTO noticeDTO = new NoticeDTO();
        noticeDTO.setId(noticeEntity.getId());
        noticeDTO.setContent(noticeEntity.getContent());
        noticeDTO.setRegister_time(noticeEntity.getRegister_time());
        noticeDTO.setSend_time(noticeEntity.getSend_time());
        noticeDTO.setWritter(noticeEntity.getWritter());
        noticeDTO.setSender(noticeEntity.getSender());
        noticeDTO.setNotification(noticeEntity.getNotification());
        return noticeDTO;
    }
}
