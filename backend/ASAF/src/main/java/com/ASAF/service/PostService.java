package com.ASAF.service;

import com.ASAF.dto.ImageDTO;
import com.ASAF.dto.PostDTO;
import com.ASAF.entity.ImageEntity;
import com.ASAF.entity.MemberEntity;
import com.ASAF.entity.PostEntity;
import com.ASAF.repository.ImageRepository;
import com.ASAF.repository.MemberRepository;
import com.ASAF.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ImageRepository imageRepository;

    // 게시글 등록 post
    @Transactional
    public void savePost(PostDTO postDTO, Optional<List<MultipartFile>> imageFiles) {
        MemberEntity memberEntity = memberRepository.findById(postDTO.getId()).orElseThrow(() -> new RuntimeException("MemberEntity not found for the given userId"));

        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(postDTO.getTitle());
        postEntity.setRegister_time(postDTO.getRegister_time());
        postEntity.setContent(postDTO.getContent());
        postEntity.setId(postDTO.getId());
        postEntity.setProfile_image(memberEntity.getProfile_image());
        postEntity.setName(memberEntity.getMemberName());
        // 게시글 저장
        PostEntity savedPost = postRepository.save(postEntity);

        if (imageFiles.isPresent()) {
            for (MultipartFile imageFile : imageFiles.get()) {
                ImageEntity imageEntity = new ImageEntity();
                String imagePath = storeImage(imageFile, savedPost.getPost_id());
                if (imagePath == null) {
                    throw new RuntimeException("Failed to store image");
                }
                imageEntity.setImageUri(imagePath);
                imageEntity.setPost(postEntity);

                imageRepository.save(imageEntity);
            }
        }
    }
    private String storeImage(MultipartFile imageFile, long post_id) {
        String storageDirectory = "/home/ubuntu/statics/images/post_images/";
        String fileName = imageFile.getOriginalFilename();
        String imagePath = null;

        try {
            if (imageFile.isEmpty()) {
                return imagePath;
            }
            imagePath = storageDirectory + post_id + "_" + fileName;
            File dest = new File(imagePath);
            FileCopyUtils.copy(imageFile.getBytes(), dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    // 전체 게시물 get 요청
    public List<PostDTO> getAllPosts() {
        List<PostEntity> postEntities = postRepository.findAll();
        List<PostDTO> result = new ArrayList<>();

        for (PostEntity postEntity : postEntities) {
            PostDTO postDTO = new PostDTO(postEntity);
            result.add(postDTO);
        }
        return result;
    }

    // 특정 게시글 get 요청
    public PostDTO getPostById(Long postId) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        List<ImageEntity> imageEntities = imageRepository.findByPost(postEntity);
        List<ImageDTO> imageDTOs = new ArrayList<>();
        if (imageEntities != null) { // 이미지 데이터가 있으면 추가합니다.
            for (ImageEntity imageEntity : imageEntities) {
                ImageDTO imageDTO = new ImageDTO(imageEntity);
                imageDTOs.add(imageDTO);
            }
        }
        PostDTO postDTO = new PostDTO(postEntity);
        postDTO.setImages(imageDTOs);

        return postDTO;
    }

    // 게시글 삭제
    public boolean deletePost(Long postId) {
        Optional<PostEntity> postEntityOptional = postRepository.findById(postId);

        if (postEntityOptional.isPresent()) {
            PostEntity postEntity = postEntityOptional.get();

            for (ImageEntity image : postEntity.getImages()) {
                Path imagePath = Paths.get(image.getImageUri());
                try {
                    Files.deleteIfExists(imagePath);
                } catch (IOException e) {
                    throw new RuntimeException("Error deleting image: " +imagePath, e);
                }
            }
            // 이미지 데이터 삭제
            imageRepository.deleteAll(postEntity.getImages());

            // 게시글 데이터 삭제
            postRepository.deleteById(postId);
            return true;
        } else {
            return false;
        }
    }

    // 게시글 수정
    public void updatePost(Long postId, PostDTO postDTO, List<MultipartFile> imageFiles) {
        MemberEntity memberEntity = memberRepository.findById(postDTO.getId())
                .orElseThrow(() -> new RuntimeException("MemberEntity not found for the given userId"));

        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("PostEntity not found for the given postId"));

        postEntity.setTitle(postDTO.getTitle());
        postEntity.setContent(postDTO.getContent());
        postRepository.save(postEntity);

        List<ImageEntity> existingImages = postEntity.getImages();
        if (!existingImages.isEmpty()) {
            // 기존 이미지 파일 삭제
            for (ImageEntity imageEntity : existingImages) {
                Path imagePath = Paths.get(imageEntity.getImageUri());
                try {
                    Files.deleteIfExists(imagePath);
                } catch (IOException e) {
                    throw new RuntimeException("Error deleting image: " + imagePath, e);
                }
            }
            // 이미지 데이터 삭제
            imageRepository.deleteAll(existingImages);
        }
        List<ImageEntity> newImages = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            ImageEntity imageEntity = new ImageEntity();
            String imagePath = storeImage(imageFile, postEntity.getPost_id());
            if (imagePath == null) {
                throw new RuntimeException("Failed to store image");
            }
            imageEntity.setImageUri(imagePath);
            imageEntity.setPost(postEntity);
            newImages.add(imageEntity);
        }
        imageRepository.saveAll(newImages);
    }
}
