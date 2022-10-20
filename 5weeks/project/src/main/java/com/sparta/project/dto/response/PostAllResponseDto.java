package com.sparta.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostAllResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private Long Comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}