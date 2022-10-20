package com.sparta.project.service;

import com.sparta.project.dto.request.PostRequestDto;
import com.sparta.project.dto.response.CommentResponseDto;
import com.sparta.project.dto.response.PostResponseDto;
import com.sparta.project.dto.response.ResponseDto;
import com.sparta.project.entity.Comment;
import com.sparta.project.entity.Member;
import com.sparta.project.entity.Post;
import com.sparta.project.jwt.TokenProvider;
import com.sparta.project.repository.CommentRepository;
import com.sparta.project.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> createPost(PostRequestDto requestDto, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);//request로 온 토큰으로 멤버 객체 반환
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .likenum(0)
                .member(member)
                .build();
        postRepository.save(post);
        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .nickname(post.getMember().getNickname())
                        .likenum(post.getLikenum())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }


    @Transactional
    public ResponseDto<?> getPost(Long id) {
        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .commentResponseDtoList(commentByPost(post, post.getMember()))
                        .nickname(post.getMember().getNickname())
                        .likenum(post.getLikenum())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> getAllPost() {
        List<Post> postList = postRepository.findAllByOrderByModifiedAtDesc();
        List<PostResponseDto> responseDtos = new ArrayList<PostResponseDto>();
        for (Post post : postList) {
            responseDtos.add(
                    PostResponseDto.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .nickname(post.getMember().getNickname())
                            .likenum(post.getLikenum())
                            .commentResponseDtoList(commentByPost(post, post.getMember()))
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(responseDtos);
    }

    @Transactional
    public ResponseDto<Post> updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        post.update(requestDto);
        return ResponseDto.success(post);
    }

    @Transactional
    public ResponseDto<?> deletePost(Long id, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
        return ResponseDto.success("delete success");
    }

    @Transactional
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    public List<CommentResponseDto> commentByPost(Post post, Member member) {
        List<Comment> commentList = commentRepository.findAllByPost(post);

        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();

        for (Comment comment : commentList) {

            commentResponseDtos.add(
                    CommentResponseDto
                            .builder()
                            .id(comment.getId())
                            .nickname(member.getNickname())
                            .comment(comment.getComment())
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }

        return commentResponseDtos;
    }

    @Transactional
    public ResponseDto<?> getMyPage(HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        List<Post> postList = postRepository.findAllByMember(member);

        List<PostResponseDto> responseDtos = new ArrayList<>();
        for (Post post : postList) {
            responseDtos.add(
                    PostResponseDto.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .nickname(post.getMember().getNickname())
                            .likenum(post.getLikenum())
                            .commentResponseDtoList(commentByPost(post,post.getMember()))
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(responseDtos);

    }
}