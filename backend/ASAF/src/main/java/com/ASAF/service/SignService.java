package com.ASAF.service;

import com.ASAF.entity.SignEntity;
import com.ASAF.repository.SignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SignService {

//    private final SignRepository signRepository;
//
//    public void saveImageUrl(String name, String month, MultipartFile file) throws IOException, ChangeSetPersister.NotFoundException {
//        SignEntity signEntity = signRepository.findByName(name)
//                .orElseThrow(ChangeSetPersister.NotFoundException::new);
//
//        // 이미지 저장 및 엔티티 업데이트 등의 작업을 수행합니다
//
//        String UPLOAD_DIR = "src/main/resources/static/images/sign_images/";
//        String fileName = file.getOriginalFilename();
//        String filePath = UPLOAD_DIR + name + "_" + fileName;
//        File dest = new File(filePath);
//        FileCopyUtils.copy(file.getBytes(), dest);
//
//        signEntity.setMonth(month);
//        signEntity.setImage_url(filePath);
//        signRepository.save(signEntity);
//    }
}
