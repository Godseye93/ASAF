package com.ASAF.entity;

import com.ASAF.dto.SignDTO;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sign")
public class SignEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sign_id;

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
    @JoinColumn(name = "user_id")
    private DocumentEntity id;

    @Column
    private String image_url;

    @Column
    private String name;

    @Column
    private String month;

    public static SignEntity toSignEntity(SignDTO signDTO) {
        SignEntity signEntity = new SignEntity();
        signEntity.setSign_id(signDTO.getSign_id());
        signEntity.setImage_url(signDTO.getImage_url());
        signEntity.setName(signDTO.getName());
        signEntity.setMonth(signDTO.getMonth());
        return signEntity;
    }

    public static SignEntity toUpdateSignEntity(SignDTO signDTO) {
        SignEntity signEntity = new SignEntity();
        signEntity.setSign_id(signDTO.getSign_id());
        signEntity.setImage_url(signDTO.getImage_url());
        signEntity.setName(signDTO.getName());
        signEntity.setMonth(signDTO.getMonth());
        return signEntity;
    }
}