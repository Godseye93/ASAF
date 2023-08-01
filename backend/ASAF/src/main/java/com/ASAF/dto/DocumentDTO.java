package com.ASAF.dto;

import com.ASAF.entity.DocumentEntity;
import com.ASAF.entity.RegionEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DocumentDTO {
    private Long doc_id;
    private String doc_style;

    private int class_num;
    private int class_code;
    private int region_code;
    private int generation_code;
    private int id;


    public DocumentDTO(DocumentEntity documentEntity) {
        this.doc_id = documentEntity.getDoc_id();
        this.doc_style = documentEntity.getDoc_style();
        this.class_num = documentEntity.getClassInfoEntity().getClass_num();
        this.class_code = documentEntity.getClassEntity().getClass_code();
        this.region_code = documentEntity.getRegionEntity().getRegion_code();
        this.generation_code = documentEntity.getGenerationEntity().getGeneration_code();
        this.id = documentEntity.getMemberEntity().getId();
    }
    // DocumentEntity 타입의 하나의 매개변수 documentEntity를 입력으로 받고 DocumentDTO 객체를 반환합니다.
    // 이 메서드의 목적은 DocumentEntity 객체의 정보를 가져와 DocumentDTO 객체로 변환하는 것입니다.
    public static DocumentDTO toDocumentDTO(DocumentEntity documentEntity) {
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setDoc_id(documentEntity.getDoc_id());
        // ClassInfoEntity를 ClassInfoDTO로 매핑
        documentDTO.setDoc_style(documentEntity.getDoc_style());
        return documentDTO;
    }
}
