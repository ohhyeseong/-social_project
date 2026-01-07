package com.example.StudyPost.post.dto;

import com.example.StudyPost.comment.dto.CommentResponseDto;
import com.example.StudyPost.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record PostResponseDto(
        Long id,
        String title,
        String content,
        String author,
        String imageUrl, // 이미지 URL 필드 추가
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<CommentResponseDto> comments
) {
    public static PostResponseDto from(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getNickname(),
                post.getImageUrl(), // imageUrl 추가
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getComments().stream()
                        .map(CommentResponseDto::from)
                        .collect(Collectors.toList())
        );
    }
}
