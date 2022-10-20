package com.sparta.project.dto.request;

import com.sparta.project.entity.Member;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long postId;
    private String comment;
    private Member member;
}