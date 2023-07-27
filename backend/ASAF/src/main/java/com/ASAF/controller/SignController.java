package com.ASAF.controller;

import com.ASAF.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/sign")
@RestController
@RequiredArgsConstructor
public class SignController {

//    private final SignService signService;
//
//    @PostMapping("/upload-image")
//    public ResponseEntity<?> uploadImage(@RequestParam("name") String name,
//                                         @RequestParam("month") String month,
//                                         @RequestParam("file") MultipartFile file) {
//        if (file == null || file.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
//        }
//        try {
//            signService.saveImageUrl(name, month, file);
//            return ResponseEntity.ok(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
//        }
//    }
}
