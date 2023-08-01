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
    @JoinColumn(name = "class_num")
    private ClassInfoEntity class_num;

    @ManyToOne
    @JoinColumn(name = "class_code")
    private ClassInfoEntity class_code;

    @ManyToOne
    @JoinColumn(name = "region_code")
    private ClassInfoEntity region_code;

    @ManyToOne
    @JoinColumn(name = "generation_code")
    private ClassInfoEntity generation_code;

    @ManyToOne
    @JoinColumn(name = "id")
    private ClassInfoEntity id;

    @Column
    private String doc_style;

    public static DocumentEntity toDocumentEntity(DocumentDTO documentDTO) {
        DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setDoc_id(documentDTO.getDoc_id());
        documentEntity.setDoc_style(documentDTO.getDoc_style());
        return documentEntity;
    }

    public static DocumentEntity toUpdateDocumentEntity(DocumentDTO documentDTO) {
        DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setDoc_id(documentDTO.getDoc_id());
        documentEntity.setDoc_style(documentDTO.getDoc_style());
        return documentEntity;
    }
}
