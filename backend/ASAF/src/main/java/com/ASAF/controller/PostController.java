package com.ASAF.controller;

import com.ASAF.dto.ImageDTO;
import com.ASAF.dto.PostDTO;
import com.ASAF.repository.PostRepository;
import com.ASAF.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@RequestMapping("/post")
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/register")
    public ResponseEntity<Boolean> savePost(@RequestPart("postDTO") String postDTOJson, @RequestPart(value = "ImageFiles", required = false) Optional<List<MultipartFile>> imageFiles) {
        ObjectMapper objectMapper = new ObjectMapper();
        PostDTO postDTO;
        try {
            postDTO = objectMapper.readValue(postDTOJson, PostDTO.class);
            postService.savePost(postDTO, imageFiles);
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
//        postService.savePost(postDTO, imageFiles);
//        return new ResponseEntity<>(HttpStatus.CREATED);
    }
//    @PostMapping("/register")
//    public ResponseEntity<Void> savePost(@RequestParam("title") String title,
//                                         @RequestParam("content") String content,
//                                         @RequestParam("register_time") long register_time,
//                                         @RequestParam("id") Long memberId,
//                                         @RequestPart("imageFiles") List<MultipartFile> imageFiles) {
//
//        postService.savePost(title, content, register_time, memberId, imageFiles);
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public PostDTO getpost(@PathVariable Long postId) {
        return postService.getpost(postId);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePostWithImages(@PathVariable Long postId, @ModelAttribute PostDTO postDTO, @RequestPart("imageFiles") List<MultipartFile> imageFiles) {
        postService.updatePost(postId, postDTO, imageFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
