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
    private ClassEntity class_code;

    @ManyToOne
    @JoinColumn(name = "region_code")
    private RegionEntity region_code;

    @ManyToOne
    @JoinColumn(name = "generation_code")
    private GenerationEntity generation_code;

    @ManyToOne
    @JoinColumn(name = "id")
    private MemberEntity id;

    @Column
    private String doc_style;

    public ClassInfoEntity getClassInfoEntity() {
        return class_num;
    }
    public ClassEntity getClassEntity() {
        return class_code;
    }
    public RegionEntity getRegionEntity() {
        return region_code;
    }
    public GenerationEntity getGenerationEntity() {
        return generation_code;
    }
    public MemberEntity getMemberEntity() {
        return id;
    }

    public static DocumentEntity toDocumentEntity(DocumentDTO documentDTO) {
        DocumentEntity documentEntity = new DocumentEntity();
        return documentEntity;
    }

    public static DocumentEntity toUpdateDocumentEntity(DocumentDTO documentDTO) {
        DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setDoc_id(documentDTO.getDoc_id());
        return documentEntity;
    }
}
