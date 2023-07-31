package com.ASAF.controller;

import com.ASAF.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;

@RequestMapping("/sign")
@RestController
@RequiredArgsConstructor
public class SignController {

    private final SignService signService;

    // 클라이언트가 저장 요청했을 때
    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("name") String name,
                                         @RequestParam("month") String month,
                                         @RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
        try {
            signService.saveImageUrl(name, month, file);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    // 클라이언트가 이미지를 요청할때
    @GetMapping("/{name}/image")
    public ResponseEntity<Resource> getSignImage(@PathVariable String name) {
        try {
            String imagePath = signService.getImageUrlPath(name);
            Resource image = new UrlResource(Paths.get(imagePath).toUri());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentDisposition(ContentDisposition.builder("inline")
                    .filename(image.getFilename())
                    .build());
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
