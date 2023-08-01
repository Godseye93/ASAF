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
    private Long notice_id;

    @ManyToOne
    @JoinColumn(name = "doc_id")
    private DocumentEntity doc_id;

    @ManyToOne
    @JoinColumn(name = "class_num")
    private DocumentEntity class_num;

    @ManyToOne
    @JoinColumn(name = "class_code")
    private DocumentEntity class_code;

    @ManyToOne
    @JoinColumn(name = "region_code")
    private DocumentEntity region_code;

    @ManyToOne
    @JoinColumn(name = "generation_code")
    private DocumentEntity generation_code;

    @ManyToOne
    @JoinColumn(name = "id")
    private DocumentEntity id;

    @Column
    private String content;

    @Column
    private Date register_time;

    @Column
    private Date send_time;

    @Column
    private String writter;

    public static NoticeEntity toNoticeEntity(NoticeDTO noticeDTO) {
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setNotice_id(noticeDTO.getNotice_id());
        noticeEntity.setContent(noticeDTO.getContent());
        noticeEntity.setRegister_time(noticeDTO.getRegister_time());
        noticeEntity.setSend_time(noticeDTO.getSend_time());
        noticeEntity.setWritter(noticeDTO.getWritter());
        return noticeEntity;
    }

    public static NoticeEntity toUpdateNoticeEntity(NoticeDTO noticeDTO) {
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setNotice_id(noticeDTO.getNotice_id());
        noticeEntity.setContent(noticeDTO.getContent());
        noticeEntity.setRegister_time(noticeDTO.getRegister_time());
        noticeEntity.setSend_time(noticeDTO.getSend_time());
        noticeEntity.setWritter(noticeDTO.getWritter());
        return noticeEntity;
    }
}
