package com.sparta.project.controller;

import com.sparta.project.dto.request.PostRequestDto;
import com.sparta.project.dto.response.ResponseDto;
import com.sparta.project.repository.PostRepository;
import com.sparta.project.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    @PostMapping( "/api/auth/post")
    public ResponseDto<?> createPost(@RequestBody PostRequestDto requestDto,
                                     HttpServletRequest request) {
        return postService.createPost(requestDto, request);
    }

    @GetMapping( "/api/post/{id}")
    public ResponseDto<?> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @GetMapping("/api/post")
    public ResponseDto<?> getAllPosts() {
        return postService.getAllPost();
    }

    @PutMapping( "/api/auth/post/{id}")
    public ResponseDto<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto,
                                     HttpServletRequest request) {
        return postService.updatePost(id, postRequestDto, request);
    }

    @DeleteMapping("/api/auth/post/{id}")
    public ResponseDto<?> deletePost(@PathVariable Long id,
                                     HttpServletRequest request) {
        return postService.deletePost(id, request);
    }

    @GetMapping( "/api/member/mypage")
    public ResponseDto<?> mypage(HttpServletRequest request) {
        return postService.getMyPage(request);
    }

}