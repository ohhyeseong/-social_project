package com.example.StudyPost.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserCreateRequestDto(

        @NotBlank(message = "아이디는 필수 입력입니다.")
        String username,

        @NotBlank(message = "비밀번호는 필수 입력입니다.")
        String password,

        @NotBlank(message = "닉네임은 필수 입력입니다.")
        String nickname
) {
}
