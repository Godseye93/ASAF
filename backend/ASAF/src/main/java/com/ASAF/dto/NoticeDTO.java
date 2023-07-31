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
    private Long notice_id;
    private int doc_id;
    private int class_num;
    private int class_code;
    private int region_code;
    private int generation_code;
    private String user_id;
    private String content;
    private Date register_time;
    private Date send_time;
    private String writter;

    public NoticeDTO(NoticeEntity noticeEntity) {
        this.notice_id = noticeEntity.getNotice_id();
        this.content = noticeEntity.getContent();
        this.register_time = noticeEntity.getRegister_time();
        this.send_time = noticeEntity.getSend_time();
        this.writter = noticeEntity.getWritter();
    }

    public static NoticeDTO toNoticeDTO(NoticeEntity noticeEntity) {
        NoticeDTO noticeDTO = new NoticeDTO();
        noticeDTO.setNotice_id(noticeEntity.getNotice_id());
        noticeDTO.setContent(noticeEntity.getContent());
        noticeDTO.setRegister_time(noticeEntity.getRegister_time());
        noticeDTO.setSend_time(noticeEntity.getSend_time());
        noticeDTO.setWritter(noticeEntity.getWritter());
        return noticeDTO;
    }
}
