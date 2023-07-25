package com.ASAF.entity;

import com.ASAF.dto.DocumentDTO;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "document")
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doc_id;

    // ClassInfoEntity를 참조하는 컬럼들
    @ManyToOne
    private ClassInfoEntity class_num;

    @ManyToOne
    private ClassInfoEntity class_code;

    @ManyToOne
    private ClassInfoEntity region_code;

    @ManyToOne
    private ClassInfoEntity generation_code;

    @ManyToOne
    private ClassInfoEntity user_id;

    @Column
    private String doc_style;

    public static DocumentEntity toDocumentEntity(DocumentDTO documentDTO) {
        DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setDoc_id(documentDTO.getDoc_id());
        // ClassInfoDTO를 ClassInfoEntity로 매핑
        documentEntity.setClass_num(ClassInfoEntity.toClassInfoEntity(documentDTO.getClassInfoDTO()));
        documentEntity.setClass_code(ClassInfoEntity.toClassInfoEntity(documentDTO.getClassInfoDTO()));
        documentEntity.setRegion_code(ClassInfoEntity.toClassInfoEntity(documentDTO.getClassInfoDTO()));
        documentEntity.setGeneration_code(ClassInfoEntity.toClassInfoEntity(documentDTO.getClassInfoDTO()));
        documentEntity.setUser_id(ClassInfoEntity.toClassInfoEntity(documentDTO.getClassInfoDTO()));
        documentEntity.setDoc_style(documentDTO.getDoc_style());
        return documentEntity;
    }
}
