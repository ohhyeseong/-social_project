package com.example.StudyPost.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateRequestDto(

        @NotBlank(message = "댓글 내용은 필수입니다.")
        @Size(max = 150, min = 1, message = "최대 150자 미만, 최소 1자 초과해 작성해주세요.")
        String content
) {
}
