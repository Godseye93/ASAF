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

    private final SignRepository signRepository;

    // post 정보 받아서 DB에 저장하기
    public void saveImageUrl(String name, String month, MultipartFile file) throws IOException {
        SignEntity signEntity = signRepository.findByName(name).orElse(null);

        // 이미지 저장 및 엔티티 업데이트 등의 작업을 수행합니다
        String UPLOAD_DIR = "src/main/resources/static/images/sign_images/";
        String fileName = file.getOriginalFilename();
        String filePath = UPLOAD_DIR + name + "_" + fileName;
        File dest = new File(filePath);
        FileCopyUtils.copy(file.getBytes(), dest);

        if (signEntity == null) {
            // 새로운 엔티티를 생성하여 저장합니다
            signEntity = new SignEntity();
            signEntity.setName(name);
            signEntity.setMonth(month);
        } else {
            signEntity.setMonth(month);
        }

        signEntity.setImage_url(filePath);
        signRepository.save(signEntity);
    }

    // get 으로 클라이언트한테 보내기
    public String getImageUrlPath(String name) throws ChangeSetPersister.NotFoundException {
        SignEntity signEntity = signRepository.findByName(name)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
        return signEntity.getImage_url();
    }
}
