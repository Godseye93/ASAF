package com.ASAF.controller;

import com.ASAF.dto.DocumentDTO;
import com.ASAF.entity.DocumentEntity;
import com.ASAF.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/document")
@RestController
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    // 문서 등록
    @PostMapping("/register")
    public DocumentDTO registerDocument(@RequestBody DocumentDTO documentDTO) {
        return documentService.registerDocument(documentDTO);
    }

    // 문서 정보 수정
    @PutMapping("/edit/{doc_id}")
    public DocumentDTO updateDocument(@PathVariable long doc_id, @RequestBody DocumentDTO documentDTO) {
        return documentService.updateDocument(doc_id, documentDTO);
    }

    // 문서 삭제
    @DeleteMapping("/delete/{doc_id}")
    public ResponseEntity<String> deleteDocument(@PathVariable long doc_id) {
        documentService.deleteDocument(doc_id);
        String message = String.format("%d번 문서 삭제성공", doc_id);
        return ResponseEntity.ok(message);
    }

    // 문서 정보 가져오기 (전체)
    @GetMapping
    public List<DocumentDTO> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    // 문서 정보 가져오기 (한개)
    @GetMapping("/{doc_id}")
    public DocumentDTO getDocument(@PathVariable long doc_id) {
        return documentService.getDocument(doc_id);
    }
}
