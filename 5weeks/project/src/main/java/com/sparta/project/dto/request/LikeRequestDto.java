package com.sparta.project.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeRequestDto {

    private Long postId;

    private Long memberId;
}
