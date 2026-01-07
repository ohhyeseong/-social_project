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
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<CommentResponseDto> comments // 댓글 목록 추가
) {
    public static PostResponseDto from(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getNickname(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                // comments 리스트를 CommentResponseDto 리스트로 변환
                post.getComments().stream()
                        .map(CommentResponseDto::from)
                        .collect(Collectors.toList())
        );
    }
}
