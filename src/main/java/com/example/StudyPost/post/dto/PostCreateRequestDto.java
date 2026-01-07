package com.example.StudyPost.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostCreateRequestDto(

        @NotBlank(message = "제목은 필수 입력란입니다.")
        @Size(max = 200, message = "title은 최대 200자 입니다.")
        String title,

        @NotBlank(message = "내용은 필수 입력란입니다.")
        String content
) {

}
