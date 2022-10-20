package com.sparta.project.dto.response;

import com.sparta.project.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String nickname;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


}
