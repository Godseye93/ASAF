package com.ASAF.service;

import com.ASAF.dto.DocumentDTO;
import com.ASAF.entity.ClassInfoEntity;
import com.ASAF.entity.DocumentEntity;
import com.ASAF.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentService {

    private final DocumentRepository documentRepository;

    // 문서 등록
    public DocumentDTO registerDocument(DocumentDTO documentDTO) {
        DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setDoc_id(documentDTO.getDoc_id());
        documentEntity.setDoc_style(documentDTO.getDoc_style());
        DocumentEntity saveDocumentEntity = documentRepository.save(documentEntity);
        documentDTO.setDoc_id(saveDocumentEntity.getDoc_id());
        return documentDTO;
    }

    // 문서 정보 수정
    public DocumentDTO updateDocument(long docId, DocumentDTO documentDTO) {
        DocumentEntity documentEntity = documentRepository.findById(docId).orElseThrow(() -> new RuntimeException("문서를 찾을 수 없습니다."));
        documentEntity.setDoc_style(documentDTO.getDoc_style());
        DocumentEntity updatedDocumentEntity = documentRepository.save(documentEntity);
        return new DocumentDTO(updatedDocumentEntity);
    }

    // 문서 삭제
    public void deleteDocument(long docId) {
        documentRepository.deleteById(docId);
    }

    // 문서 정보 가져오기(전체)
    public List<DocumentDTO> getAllDocuments() {
        List<DocumentEntity> documentEntities = documentRepository.findAllOrderByCreatedAtDesc();
        return documentEntities.stream()
                .map(documentEntity -> new DocumentDTO(documentEntity))
                .collect(Collectors.toList());
    }


    // 문서 정보 가지오기(하나)
    public DocumentDTO getDocument(long docId) {
        DocumentEntity documentEntity = documentRepository.findById(docId).orElseThrow(() -> new RuntimeException("문서를 찾을 수 없습니다."));
        return new DocumentDTO(documentEntity);
    }
}
