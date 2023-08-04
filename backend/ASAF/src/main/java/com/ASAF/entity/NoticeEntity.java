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
    private String title;

    @Column
    private String content;

    @Column
    private long registerTime;

    @Column
    private long send_time;

    @Column
    private int sender;

    @Column
    private int receiver;

    @Column
    private Boolean notification;

    public static NoticeEntity toNoticeEntity(NoticeDTO noticeDTO) {
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setTitle(noticeDTO.getTitle());
        noticeEntity.setContent(noticeDTO.getContent());
        noticeEntity.setRegisterTime(noticeDTO.getRegisterTime());
        noticeEntity.setSend_time(noticeDTO.getSend_time());
        noticeEntity.setReceiver(noticeDTO.getReceiver());
        noticeEntity.setSender(noticeDTO.getSender());
        noticeEntity.setNotification(noticeDTO.getNotification());
        return noticeEntity;
    }

    public static NoticeEntity toUpdateNoticeEntity(NoticeDTO noticeDTO) {
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setId(noticeDTO.getId());
        noticeEntity.setTitle(noticeDTO.getTitle());
        noticeEntity.setContent(noticeDTO.getContent());
        noticeEntity.setRegisterTime(noticeDTO.getRegisterTime());
        noticeEntity.setSend_time(noticeDTO.getSend_time());
        noticeEntity.setReceiver(noticeDTO.getReceiver());
        noticeEntity.setSender(noticeDTO.getSender());
        noticeEntity.setNotification(noticeDTO.getNotification());
        return noticeEntity;
    }
}
