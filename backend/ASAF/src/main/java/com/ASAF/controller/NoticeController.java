package com.ASAF.controller;

import com.ASAF.dto.NoticeDTO;
import com.ASAF.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

//    @PostMapping
//    public ResponseEntity<NoticeDTO> createNotice(@RequestBody NoticeDTO noticeDTO) {
//        return ResponseEntity.ok(noticeService.createNotice(noticeDTO));
//    }

//    @PostMapping
//    public ResponseEntity<NoticeDTO> createNotice(@RequestBody NoticeDTO noticeDTO) {
//        NoticeDTO result = noticeService.createNotice(noticeDTO);
//
//        if (result == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        } else {
//            return ResponseEntity.ok(result);
//        }
//    }
    @PostMapping
    public ResponseEntity<List<NoticeDTO>> createNotices(@RequestBody List<NoticeDTO> noticeDTOs) {
        List<NoticeDTO> results = noticeService.createNotices(noticeDTOs);

        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.ok(results);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoticeDTO> updateNotice(@PathVariable int id, @RequestBody NoticeDTO noticeDTO) {
        noticeDTO.setId(id);
        return ResponseEntity.ok(noticeService.updateNotice(noticeDTO));
    }

    @GetMapping
    public ResponseEntity<List<NoticeDTO>> getAllNotices() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeDTO> getNoticeById(@PathVariable int id) {
        NoticeDTO noticeDTO = noticeService.getNoticeById(id);
        if (noticeDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(noticeDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable int id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.ok().build();
    }
}
