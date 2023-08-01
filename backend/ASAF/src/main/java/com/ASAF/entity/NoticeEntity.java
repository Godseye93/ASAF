package com.ASAF.entity;

import com.ASAF.dto.NoticeDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "notice")
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String Title;

    @Column
    private String content;

    @Column
    private Date register_time;

    @Column
    private Date send_time;

    @Column
    private String writter;

    @Column
    private int sender;

    @Column
    private Boolean notification;

    public static NoticeEntity toNoticeEntity(NoticeDTO noticeDTO) {
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setTitle(noticeDTO.getTitle());
        noticeEntity.setContent(noticeDTO.getContent());
        noticeEntity.setRegister_time(noticeDTO.getRegister_time());
        noticeEntity.setSend_time(noticeDTO.getSend_time());
        noticeEntity.setWritter(noticeDTO.getWritter());
        noticeEntity.setSender(noticeDTO.getSender());
        noticeEntity.setNotification(noticeDTO.getNotification());
        return noticeEntity;
    }

    public static NoticeEntity toUpdateNoticeEntity(NoticeDTO noticeDTO) {
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setId(noticeDTO.getId());
        noticeEntity.setTitle(noticeDTO.getTitle());
        noticeEntity.setContent(noticeDTO.getContent());
        noticeEntity.setRegister_time(noticeDTO.getRegister_time());
        noticeEntity.setSend_time(noticeDTO.getSend_time());
        noticeEntity.setWritter(noticeDTO.getWritter());
        noticeEntity.setSender(noticeDTO.getSender());
        noticeEntity.setNotification(noticeDTO.getNotification());
        return noticeEntity;
    }
}
