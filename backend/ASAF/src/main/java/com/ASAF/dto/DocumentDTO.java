package com.ASAF.dto;

import com.ASAF.entity.ClassInfoEntity;
import com.ASAF.entity.DocumentEntity;
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
    private ClassInfoDTO classInfoDTO;
    private String doc_style;

    // DocumentEntity 타입의 하나의 매개변수 documentEntity를 입력으로 받고 DocumentDTO 객체를 반환합니다.
    // 이 메서드의 목적은 DocumentEntity 객체의 정보를 가져와 DocumentDTO 객체로 변환하는 것입니다.
    public static DocumentDTO toDocumentDTO(DocumentEntity documentEntity) {
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setDoc_id(documentEntity.getDoc_id());
        // ClassInfoEntity를 ClassInfoDTO로 매핑
        documentDTO.setClassInfoDTO(ClassInfoDTO.toClassInfoDTO(documentEntity.getClass_num()));
        documentDTO.setDoc_style(documentEntity.getDoc_style());
        return documentDTO;
    }
}
