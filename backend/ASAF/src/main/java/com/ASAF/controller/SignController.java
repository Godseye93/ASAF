package com.ASAF.controller;

import com.ASAF.dto.SignDTO;
import com.ASAF.service.SignService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.PropertySource;
import org.codehaus.jackson.JsonProcessingException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/sign")
@RestController
@RequiredArgsConstructor
public class SignController {

    private final SignService signService;

    // 클라이언트가 저장 요청했을 때
    @PostMapping("/upload-image")
    public ResponseEntity<Boolean> uploadImage(@RequestPart("signDTO") String signDTOJson, @RequestPart("ImageFile") MultipartFile imageFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        SignDTO signDTO;

        try {
            signDTO = objectMapper.readValue(signDTOJson, SignDTO.class);
            signService.saveImageUrl(signDTO, imageFile);
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
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

    // 서명서 이미지, 이름 반환
    @GetMapping("/classCodes")
    public List<SignDTO> getSignsByCodes(@RequestParam("class_code") int class_code,
                                         @RequestParam("region_code") int region_code,
                                         @RequestParam("generation_code") int generation_code,
                                         @RequestParam("month") String month) throws ChangeSetPersister.NotFoundException {
        System.out.println("통신 확인");
        List<SignDTO> signs = signService.getSignsByCodes(class_code, region_code, generation_code, month);
        System.out.println(signs.get(0).toString());
        // 이름순으로 정렬
        signs = signs.stream()
                .sorted(Comparator.comparing(SignDTO::getName))
                .collect(Collectors.toList());
        return signs;
    }
}
