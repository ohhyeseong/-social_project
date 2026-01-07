package com.example.StudyPost.comment.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentCreateRequestDto(
        @NotBlank(message = "댓글 내용은 필수입니다.")
        String content
) {
}
