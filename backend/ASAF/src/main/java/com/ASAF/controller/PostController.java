package com.ASAF.controller;

import com.ASAF.dto.ImageDTO;
import com.ASAF.dto.PostDTO;
import com.ASAF.repository.PostRepository;
import com.ASAF.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.swing.interop.SwingInterOpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.MalformedURLException;
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
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {
        PostDTO postDTO = postService.getPostById(postId);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Boolean> deletePost(@PathVariable Long postId) {
        boolean result = postService.deletePost(postId);
        if (result) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePostWithImages(@PathVariable Long postId, @ModelAttribute PostDTO postDTO, @RequestPart("imageFiles") List<MultipartFile> imageFiles) {
        postService.updatePost(postId, postDTO, imageFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
